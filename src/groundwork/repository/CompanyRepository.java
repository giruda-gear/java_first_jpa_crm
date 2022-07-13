package com.aaihc.crm.biz.groundwork.repository;

import asn.util.lang.StringUtil;
import com.aaihc.crm.biz.customized.domain.QCustomized;
import com.aaihc.crm.biz.groundwork.domain.Company;
import com.aaihc.crm.core.domain.Search;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.aaihc.crm.biz.groundwork.domain.QAffiliate.affiliate;
import static com.aaihc.crm.biz.groundwork.domain.QCompany.company;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CompanyRepository (고객사 Repository)</p>
 *
 * @author 	    : 양용수
 * date 		: 2021. 03. 08.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Repository
public class CompanyRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CompanyRepository(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(Company.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * <p>고객사을 등록합니다</p>
     *
     * @param company 고객사
     */
    public void save(Company company) {
        em.persist(company);
    }

    /**
     * <p>지정된 고객사을 조회합니다.</p>
     *
     * @param cd 코드
     * @return Optional Company
     */
    public Company findById(String cd) {
        return em.find(Company.class, cd);
    }

    /**
     * <p>지정된 고객사을 삭제합니다.</p>
     *
     * @param cd 코드
     * @return 수
     */
    public int deleteById(String cd) {
        int result = 0;

        if (StringUtil.isNotBlank(cd)) {
            Company company = this.findById(cd);
            if (company != null) {
                em.remove(company);
                result = 1;
            }
        }

        return result;
    }

    /**
     * <p>지정된 고객사의 개수를 조회합니다.</p>
     *
     * @param search 검색
     * @return 수
     */
    public long count(Search search) {
        return this.getQuery(search).fetchCount();
    }

    /**
     * <p>지정된 고객사의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<Company> findAll(Search search) {
        return this.getQuery(search).fetch();
    }

    /**
     * <p>지정된 고객사의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public Page<Company> findPage(Search search) {
        Pageable pageable = search.getPageable();
        QueryResults<Company> result = this.getQuery(search).fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    /**
     * <p>쿼리를 구합니다.</p>
     *
     * @param search 검색
     * @return 값
     */
    private JPQLQuery<Company> getQuery(Search search) {
        JPQLQuery<Company> query = queryFactory.selectFrom(company);

        String fthJnYn = search.getString("fthJnYn");
        if (StringUtil.equals(fthJnYn, "Y")) {
            String fthJnEntt1 = search.getString("fthJnEntt1");
            if (StringUtil.equals("goods", fthJnEntt1)) {
                query.leftJoin(company.goodses).fetchJoin();
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

        String schTxt = search.getString("schTxt");
        String schFld = search.getString("schFld");

        if (StringUtil.isNotBlank(schTxt)) {
            switch (schFld) {
                case "whle" :
                    schConds.add(company.nm.containsIgnoreCase(schTxt).or(company.cd.containsIgnoreCase(schTxt)));
                    break;
                case "nm" :
                    schConds.add(company.nm.containsIgnoreCase(schTxt));
                    break;
                case "cd" :
                    schConds.add(company.cd.containsIgnoreCase(schTxt));
                    break;
                default:
                    break;
            }
        }
        
        String cd = search.getString("cd").replace(" ", "");
        if (StringUtil.isNotBlank(cd)) {
            schConds.add(company.cd.equalsIgnoreCase((cd)));
        }

        String notCd = search.getString("notCd").replace(" ", "");
        if (StringUtil.isNotBlank(cd)) {
            schConds.add(company.cd.notEqualsIgnoreCase(notCd));
        }

        String[] cds = (String[]) search.get("cds");
        if (cds != null) {
            schConds.add(company.cd.in((cds)));
        }

        Object cds2 = search.get("cds2");
        if (cds2 != null) {
            schConds.add(company.cd.in((Collection<? extends String>) cds2));
        }

        String nm = search.getString("nm");
        if (StringUtil.isNotBlank(nm)) {
            schConds.add(company.nm.eq(nm));
        }

        String coCdLk = search.getString("coCdLk");
        if (StringUtil.isNotBlank(coCdLk)) {
            schConds.add(company.cd.containsIgnoreCase(coCdLk));
        }

        String coNmLk = search.getString("coNmLk");
        if (StringUtil.isNotBlank(coNmLk)) {
            schConds.add(company.nm.containsIgnoreCase(coNmLk));
        }

        String useYn = search.getString("useYn");
        if (StringUtil.isNotBlank(useYn)) {
            schConds.add(company.useYn.eq(useYn));
        }

        Object custDestrDayGe = search.get("custDestrDayGe");
        if (custDestrDayGe != null) {
            schConds.add(company.custDestrDay.goe((int) custDestrDayGe));
        }

        // 해외상담 내역 조회시 전용고객사 검색
        String schCoNm = search.getString("schCoNm");
        if (StringUtil.isNotBlank(schCoNm) && StringUtil.equals(schCoNm, "frgn")) {
            schConds.add(company.nm.contains("해외"));
        }

        // 맞춤서비스에 등록된 고객사 조회
        String schTbCzSvcWithYn = search.getString("schTbCzSvcWithYn");
        if (StringUtil.equals("Y", schTbCzSvcWithYn)) {
            List<String> cds_ = queryFactory.select(QCustomized.customized.coCd).from(QCustomized.customized).fetch();
            List<String> cds2_ = cds_.stream().distinct().collect(Collectors.toList());
            schConds.add(company.cd.in(cds2_));
        }

        // 맞춤서비스에 등록된 고객사 조회
        String schTbPcWithYn = search.getString("schTbPcWithYn");
        if (StringUtil.equals("Y", schTbCzSvcWithYn)) {
            List<String> cds_ = queryFactory.select(QCustomized.customized.coCd).from(QCustomized.customized).fetch();
            List<String> cds2_ = cds_.stream().distinct().collect(Collectors.toList());
            schConds.add(company.cd.in(cds2_));
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
        List<OrderSpecifier> orderSpecifiers = search.getQdslSortConds(company);

        String sortFld = search.getString("sortFld");
        String sortMetd = search.getString("sortMetd");
        if (StringUtil.isNotBlank(sortFld) && StringUtil.endsWith(sortFld, "CT")) {
            if (StringUtil.equals("useYnCT", sortFld)) {
                NumberExpression<Integer> currency = new CaseBuilder().when(company.useYn.eq("Y")).then(1)
                        .when(company.useYn.eq("N")).then(2)
                        .otherwise(3);

                if (StringUtil.equals("ASC", sortMetd)) {
                    orderSpecifiers.add(currency.asc());
                    orderSpecifiers.add(company.cd.asc());
                } else {
                    orderSpecifiers.add(currency.desc());
                    orderSpecifiers.add(company.cd.asc());
                }
            }
        }

        if (orderSpecifiers.size() == 0) {
            orderSpecifiers.add(Search.getQdslSortedColumn(Order.ASC, company, "cd"));
        }

        return orderSpecifiers;
    }

}