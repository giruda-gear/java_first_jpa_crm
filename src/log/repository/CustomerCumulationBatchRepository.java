package com.aaihc.crm.biz.log.repository;

import asn.util.lang.StringUtil;
import com.aaihc.crm.biz.log.domain.CustomerCumulationBatch;
import com.aaihc.crm.biz.log.domain.CustomerCumulationBatchGrpRnkDto;
import com.aaihc.crm.core.domain.Search;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static com.aaihc.crm.biz.log.domain.QCustomerCumulationBatch.customerCumulationBatch;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCumulationBatchRepository (고객 누적 배치 Repository)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 11.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Repository
public class CustomerCumulationBatchRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CustomerCumulationBatchRepository(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(CustomerCumulationBatch.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * <p>고객 누적 배치을 등록합니다</p>
     *
     * @param customerCumulationBatch 고객 누적 배치
     */
    public void save(CustomerCumulationBatch customerCumulationBatch) {
        em.persist(customerCumulationBatch);
    }

    /**
     * <p>지정된 고객 누적 배치을 조회합니다.</p>
     *
     * @param seq 일련번호
     * @return Optional CustomerCumulationBatch
     */
    public CustomerCumulationBatch findById(long seq) {
        return em.find(CustomerCumulationBatch.class, seq);
    }

    /**
     * <p>지정된 고객 누적 배치을 삭제합니다.</p>
     *
     * @param seq 일련번호
     * @return 수
     */
    public int deleteById(long seq) {
        int result = 0;

        if (seq > 0) {
            CustomerCumulationBatch customerCumulationBatch = this.findById(seq);
            if (customerCumulationBatch != null) {
                em.remove(customerCumulationBatch);
                result = 1;
            }
        }

        return result;
    }

    /**
     * <p>지정된 고객 누적 배치의 개수를 조회합니다.</p>
     *
     * @param search 검색
     * @return 수
     */
    public long count(Search search) {
        return this.getQuery(search).fetchCount();
    }

    /**
     * <p>지정된 고객 누적 배치의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCumulationBatch> findAll(Search search) {
        return this.getQuery(search).fetch();
    }

    /**
     * <p>지정된 고객 누적 배치의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCumulationBatchGrpRnkDto> findAllGrpRnk1(Search search) {
        StringBuffer sbSch = new StringBuffer();

        String year = search.getString("year");
        if (StringUtil.isNotBlank(year)) {
            sbSch.append("       and left(ccb_cust_tret_ymd, 4) = :year                ");
        }
        String coCd = search.getString("coCd");
        if (StringUtil.isNotBlank(coCd)) {
            sbSch.append("       and co_cd = :coCd                                      ");
        }
        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            sbSch.append("       and gd_cd = :gdCd                                    ");
        }
        String custTretStrtYmd = search.getString("custTretStrtYmd");
        if (StringUtil.isNotBlank(custTretStrtYmd)) {
            sbSch.append("       and ccb_cust_tret_ymd >= :custTretStrtYmd              ");
        }
        String custTretEndYmd = search.getString("custTretEndYmd");
        if (StringUtil.isNotBlank(custTretEndYmd)) {
            sbSch.append("       and ccb_cust_tret_ymd <= :custTretEndYmd               ");
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" select 																																	    ");
        sb.append("     t1.ccb_file_cyc as fileCyc                                                                                                              ");
        sb.append("      , t1.ccb_tot_cnt as totCnt                                                                                                             ");
        sb.append("      , t1.ccb_cust_tret_ymd as custTretYmd                                                                                                  ");
        sb.append("      , t1.co_cd as coCd                                                                                                                     ");
        sb.append("      , t1.gd_cd as gdCd                                                                                                                   ");
        sb.append("      , t2.co_nm as coNm                                                                                                                     ");
        sb.append("      , t2.co_use_yn as coUseYn                                                                                                              ");
        sb.append("      , t3.gd_nm as gdNm                                                                                                                     ");
        sb.append("      , t3.gd_ctrt_use_yn as gdCtrtUseYn                                                                                                     ");
        sb.append("      , t3.gd_use_yn as gdUseYn                                                                                                              ");
        sb.append(" from                                                                                                                                        ");
        sb.append(" (                                                                                                                                           ");
        sb.append("     select                                                                                                                                  ");
        sb.append("         row_number() over(partition by ccb_file_cyc, co_cd, gd_cd, left(ccb_cust_tret_ymd, 6) order by ccb_cust_tret_ymd desc) as rnk      ");
        sb.append("          , ccb_file_cyc                                                                                                                     ");
        sb.append("          , ccb_tot_cnt                                                                                                                      ");
        sb.append("          , co_cd                                                                                                                            ");
        sb.append("          , gd_cd                                                                                                                           ");
        sb.append("          , ccb_cust_tret_ymd                                                                                                                ");
        sb.append("     from t_lo_cust_cmltn_bat                                                                                                                ");
        sb.append("     where 1 = 1                                                                                                                             ");

        sb.append(sbSch.toString());

        sb.append(" ) t1 left outer join t_gw_co t2 on t1.co_cd = t2.co_cd                                                                                      ");
        sb.append("      left outer join t_gw_gds t3 on t1.gd_cd = t3.gd_cd                                                                                   ");
        sb.append(" where t1.rnk = 1 or t1.gd_cd is null                                                                                                       ");
        sb.append(" order by t2.co_nm, t3.gd_nm asc                                                                                                             ");

        JpaResultMapper jpaResult = new JpaResultMapper();
        Query query = em.createNativeQuery(sb.toString());

        if (StringUtil.isNotBlank(year)) {
            query.setParameter("year", year);
        }
        if (StringUtil.isNotBlank(coCd)) {
            query.setParameter("coCd", coCd);
        }
        if (StringUtil.isNotBlank(gdCd)) {
            query.setParameter("gdCd", gdCd);
        }
        if (StringUtil.isNotBlank(custTretStrtYmd)) {
            query.setParameter("custTretStrtYmd", custTretStrtYmd);
        }
        if (StringUtil.isNotBlank(custTretEndYmd)) {
            query.setParameter("custTretEndYmd", custTretEndYmd);
        }

        List<CustomerCumulationBatchGrpRnkDto> results = jpaResult.list(query, CustomerCumulationBatchGrpRnkDto.class);
        return results;
    }

    /**
     * <p>지정된 고객 누적 배치의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public Page<CustomerCumulationBatch> findPage(Search search) {
        Pageable pageable = search.getPageable();
        QueryResults<CustomerCumulationBatch> result = this.getQuery(search).fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    /**
     * <p>쿼리를 구합니다.</p>
     *
     * @param search 검색
     * @return 값
     */
    private JPQLQuery<CustomerCumulationBatch> getQuery(Search search) {
        JPQLQuery<CustomerCumulationBatch> query = queryFactory.selectFrom(customerCumulationBatch);

        // 패치조인 query 추가
        String fthJnYn = search.getString("fthJnYn");
        if (StringUtil.equals("Y", fthJnYn)) {
            String fthJnEntt1 = search.getString("fthJnEntt1");
            if (StringUtil.equals("company", fthJnEntt1)) {
                query.leftJoin(customerCumulationBatch.company).fetchJoin();
            }

            String fthJnEntt2 = search.getString("fthJnEntt2");
            if (StringUtil.equals("goods", fthJnEntt2)) {
                query.leftJoin(customerCumulationBatch.goods).fetchJoin();
            }
        }

        // 검색조건 추가
        List<Predicate> schConds = this.getSchConds(search);
        if (schConds.size() > 0) {
            query.where(schConds.stream().toArray(Predicate[]::new));
        }

        // 정렬조건 추가
        List<OrderSpecifier> sortConds = this.getSortConds(search);
        if (sortConds.size() > 0) {
            query.orderBy(sortConds.stream().toArray(OrderSpecifier[]::new));
        }

        // 페이징 추가
        Pageable pageable = search.getPageable();
        if (pageable != null) {
            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());
        }

        return query;
    }

    /**
     * <p>검색조건을 생성합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    private List<Predicate> getSchConds(Search search) {
        List<Predicate> schConds = new ArrayList<>();

        String coCd = search.getString("coCd");
        if (StringUtil.isNotBlank(coCd)) {
            schConds.add(customerCumulationBatch.company.cd.equalsIgnoreCase((coCd)));
        }

        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            schConds.add(customerCumulationBatch.goods.cd.eq(gdCd));
        }

        String year = search.getString("year");
        if (StringUtil.isNotBlank(year)) {
            schConds.add(customerCumulationBatch.custTretYmd.startsWith((year)));
        }

        String custTretStrtYmd = search.getString("custTretStrtYmd");
        if (StringUtil.isNotBlank(custTretStrtYmd)) {
            schConds.add(customerCumulationBatch.custTretYmd.goe(custTretStrtYmd));
        }

        String custTretEndYmd = search.getString("custTretEndYmd");
        if (StringUtil.isNotBlank(custTretEndYmd)) {
            schConds.add(customerCumulationBatch.custTretYmd.loe(custTretEndYmd));
        }

        return schConds;
    }

    /**
     * <p>정렬조건을 생성합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    private List<OrderSpecifier> getSortConds(Search search) {
        List<OrderSpecifier> orderSpecifiers = search.getQdslSortConds(customerCumulationBatch);

        if (orderSpecifiers.size() == 0) {
            orderSpecifiers.add(Search.getQdslSortedColumn(Order.DESC, customerCumulationBatch, "seq"));
        }

        return orderSpecifiers;
    }

}