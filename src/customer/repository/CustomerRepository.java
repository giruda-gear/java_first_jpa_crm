package com.aaihc.crm.biz.customer.repository;

import asn.util.lang.StringUtil;
import com.aaihc.crm.biz.customer.domain.Customer;
import com.aaihc.crm.biz.customer.domain.dto.*;
import com.aaihc.crm.core.common.Aes256Util;
import com.aaihc.crm.core.domain.Search;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringPath;
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

import static com.aaihc.crm.biz.customer.domain.QCustomer.customer;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerRepository (고객 Repository)</p>
 *
 * @author : 양용수
 * date 		: 2021. 03. 18.
 * <p>
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Repository
public class CustomerRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CustomerRepository(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(Customer.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * <p>고객을 등록합니다</p>
     *
     * @param customer 고객
     */
    public void save(Customer customer) {
        em.persist(customer);
    }

    /**
     * <p>지정된 고객을 조회합니다.</p>
     *
     * @param seq 일련번호
     * @return Optional Customer
     */
    public Customer findById(long seq) {
        return em.find(Customer.class, seq);
    }

    /**
     * <p>지정된 고객을 삭제합니다.</p>
     *
     * @param seq 일련번호
     * @return 수
     */
    public int deleteById(long seq) {
        int result = 0;

        if (seq > 0) {
            Customer customer = this.findById(seq);
            if (customer != null) {
                em.remove(customer);
                result = 1;
            }
        }

        return result;
    }

    /**
     * <p>지정된 고객을 삭제합니다. (소멸)</p>
     *
     * @param ymd 년월일
     */
    public void deleteByExtncYn(String ymd) {
        if (StringUtil.isNotBlank(ymd)) {
            StringBuffer qy = new StringBuffer();
            qy.append(" delete from t_cu_cust ");
            qy.append(" from t_cu_cust t1 inner join t_gw_co t2 on t1.co_cd = t2.co_cd ");
            qy.append(" where t1.cu_extnc_yn = 'Y' and t2.co_cust_ascl_day >= 365 ");
            qy.append(" and convert(varchar(20), dateadd(day, t2.co_cust_ascl_day, t1.cu_extnc_ymd), 112) = :ymd ");

            Query query = em.createNativeQuery(qy.toString());
            query.setParameter("ymd", ymd);
            query.executeUpdate();

            em.flush();
            em.clear();
        }
    }

    /**
     * <p>지정된 고객의 개수를 조회합니다.</p>
     *
     * @param search 검색
     * @return 수
     */
    public long count(Search search) {
        return this.getQuery(search).fetchCount();
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<Customer> findAll(Search search) {
        return this.getQuery(search).fetch();
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerSetlObjYnDto> findAllGrpBySetlObjYn(Search search) {
        return this.getQueryGrpBySetlObjYn(search).fetch();
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerSvcAnonymousAssistanceCaseDto> findAllsvcAnonymousAssistanceCase(Search search) {
        StringBuffer sb = new StringBuffer();
        StringBuffer sbSch = new StringBuffer();

        String schFrgnYn = search.getString("schFrgnYn");

        String custNm = search.getString("custNm");
        if (StringUtil.isNotBlank(custNm)) {
            sbSch.append("  and t1.cs_cust_nm = :custNm ");
        }

        String cfTel = search.getString("cfTel");
        if (StringUtil.isNotBlank(cfTel)) {
            if (StringUtil.length(cfTel) == 4) {
                sbSch.append("  and t2.cf_tel like :cfTel ");
            } else if (StringUtil.length(cfTel) > 4) {
                sbSch.append("  and t2.cf_tel = :cfTel ");
            }
        }

        String cfCase = search.getString("cfCase");
        if (StringUtil.isNotBlank(cfCase)) {
            sbSch.append("  and t2.cf_case LIKE :cfCase ");
        }

        sb.append("\n select                                                          ");
        sb.append("\n     t1.cs_cust_nm                                               ");
        sb.append("\n     , t3.co_cd                                                  ");
        sb.append("\n     , t3.co_nm                                                  ");
        sb.append("\n     , t4.gd_cd                                                 ");
        sb.append("\n     , t4.gd_nm                                                  ");
        sb.append("\n     , t1.cf_seq                                                 ");
        sb.append("\n     , t1.cs_seq                                                 ");
        sb.append("\n     , t1.cs_rgstr_nm                                            ");
        sb.append("\n     , t1.cs_rgst_ymd                                            ");
        sb.append("\n     , t1.cs_rgst_his                                            ");
        sb.append("\n     , t1.cf_case                                                ");
        sb.append("\n     , t1.cf_nat                                                 ");
        sb.append("\n     , t1.cf_city                                                ");
        sb.append("\n     , [CRM].[dbo].[fnDecryptT](t1.cf_tel) as cf_tel               ");
        sb.append("\n     , [CRM].[dbo].[fnDecryptT](t1.cf_cabk_no) as cf_cabk_no       ");
        sb.append("\n     , [CRM].[dbo].[fnDecryptS](t1.cf_cabk_email) as cf_cabk_email ");
        sb.append("\n     , isnull(t2.cnt, 0) as cnt                                  ");
        sb.append("\n from (                                                          ");
        sb.append("\n         select *                                                ");
        sb.append("\n         from (                                                  ");

        if (StringUtil.equals(schFrgnYn, "Y")) {
            sb.append("\n                 select row_number() over (partition by t2.cf_case, t2.cf_tel, t1.gd_cd order by t1.cs_rgst_ymd desc) as rnk ");
        } else {
            sb.append("\n                 select row_number() over (partition by t2.cf_tel, t1.gd_cd order by t1.cs_rgst_ymd desc) as rnk             ");
        }

        sb.append("\n                      , t1.*                                                 ");
        sb.append("\n                      , t2.cf_seq                                            ");
        sb.append("\n                      , t2.cf_tel                                            ");
        sb.append("\n                      , t2.cf_case                                           ");
        sb.append("\n                      , t2.cf_nat                                            ");
        sb.append("\n                      , t2.cf_city                                           ");
        sb.append("\n                      , t2.cf_cabk_no                                        ");
        sb.append("\n                      , t2.cf_cabk_email                                     ");
        sb.append("\n               from t_cs_cnsl t1                                             ");
        sb.append("\n                      inner join t_cs_cnsl_frgn t2 on t1.cs_seq = t2.cs_seq  ");
        sb.append("\n               where  t1.cu_seq is null                                      ");
        sb.append("\n                      and t2.cf_tel is not null                              ");
        sb.append("\n                      and t2.cf_tel != ''                                    ");

        if (StringUtil.equals(schFrgnYn, "Y")) {
            sb.append("\n                       and t2.cf_case is not null ");
            sb.append("\n                       and t2.cf_case != ''       ");
        } else {
            sb.append("\n and t1.cs_cnsl_extnl_tp = '0051-0011' ");
        }

        sb.append(sbSch.toString());

        sb.append("\n                  ) t1                                                                ");
        sb.append("\n         where rnk = 1                                                                ");
        sb.append("\n     ) t1 left outer join (                                                           ");
        sb.append("\n         select                                                                       ");
        sb.append("\n             gd_cd, tel, sum(cnt) as cnt                                             ");
        sb.append("\n         from (                                                                       ");
        sb.append("\n             select gd_cd                                                            ");
        sb.append("\n                 , cf_tel as tel                                                      ");
        sb.append("\n                 , count(*) as cnt                                                    ");
        sb.append("\n             from t_cs_cnsl_frgn                                                      ");
        sb.append("\n             where gd_cd is not null and cf_tel is not null and cf_tel != ''         ");
        sb.append("\n             group by gd_cd, cf_tel                                                  ");
        sb.append("\n             union all                                                                ");
        sb.append("\n             select gd_cd                                                            ");
        sb.append("\n                 , cf_cabk_no as tel                                                  ");
        sb.append("\n                 , count(*) as cnt                                                    ");
        sb.append("\n             from t_cs_cnsl_frgn                                                      ");
        sb.append("\n             where gd_cd is not null and cf_cabk_no is not null and cf_cabk_no != '' ");
        sb.append("\n             group by gd_cd, cf_cabk_no                                              ");
        sb.append("\n         ) t1                                                                         ");
        sb.append("\n         group by gd_cd, tel                                                         ");
        sb.append("\n ) t2 on t1.gd_cd = t2.gd_cd and t1.cf_tel = t2.tel                                 ");
        sb.append("\n left outer join t_gw_co t3 on t1.co_cd = t3.co_cd                                    ");
        sb.append("\n left outer join t_gw_gds t4 on t1.gd_cd = t4.gd_cd                                 ");
        sb.append("\n order by cf_case desc                                                                ");


        JpaResultMapper jpaResult = new JpaResultMapper();
        Query query = em.createNativeQuery(sb.toString());

        if (StringUtil.isNotBlank(custNm)) {
            query.setParameter("custNm", custNm);
        }

        if (StringUtil.isNotBlank(cfTel)) {
            if (StringUtil.length(cfTel) == 4) {
                query.setParameter("cfTel", cfTel + "%");
            } else if (StringUtil.length(cfTel) > 4) {
                query.setParameter("cfTel", Aes256Util.fnEncryptT(cfTel));
            }
        }

        if (StringUtil.isNotBlank(cfCase)) {
            query.setParameter("cfCase", cfCase + "%");
        }

        List<CustomerSvcAnonymousAssistanceCaseDto> results = jpaResult.list(query, CustomerSvcAnonymousAssistanceCaseDto.class);

        return results;
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCoCdGdSeqSvcActDto> findAllGrpCoCdGdSeqSvcAct(Search search) {
        StringBuffer sb = new StringBuffer();
        StringBuffer sbSch = new StringBuffer();

        String coCd = search.getString("coCd");
        if (StringUtil.isNotBlank(coCd)) {
            sbSch.append("  and co_cd = :coCd       ");
        }

        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {

            sbSch.append("  and gd_cd = :gdCd    ");
        }

        sb.append(" select																																																																																				");
        sb.append("     t1.*                                                                                                                                                                      ");
        sb.append("     , t2.co_nm                                                                                                                                                                ");
        sb.append("     , t2.co_use_yn                                                                                                                                                               ");
        sb.append("     , t3.gd_nm                                                                                                                                                                ");
        sb.append("     , t3.gd_use_yn                                                                                                                                                                ");
        sb.append("     , (case when t3.gd_ctrt_ymd is not null then 'Y' else 'N' end) as gd_ctrt_yn                                                                                              ");
        sb.append(" from (                                                                                                                                                                        ");
        sb.append("     select                                                                                                                                                                    ");
        sb.append("         co_cd                                                                                                                                                                 ");
        sb.append("         , gd_cd                                                                                                                                                              ");
        sb.append("         , cu_setl_obj_yn                                                                                                                                                      ");
        sb.append("         , cu_use_stop_yn                                                                                                                                                      ");
        sb.append("         , (case when cu_rgstr_id = 'system' then 'batch' when cu_rgstr_id = 'aaisvc' then 'aaihc' else 'individual' end) as cu_act_desc                                       ");
        sb.append("         , count(*) as cnt                                                                                                                                                     ");
        sb.append("     from t_cu_cust                                                                                                                                                            ");
        sb.append("     where 1=1                                                                                                                                                                 ");

        sb.append(sbSch.toString());

        sb.append("     group by co_cd, gd_cd, cu_setl_obj_yn, cu_use_stop_yn, (case when cu_rgstr_id = 'system' then 'batch' when cu_rgstr_id = 'aaisvc' then 'aaihc' else 'individual' end)    ");
        sb.append(" ) t1 left outer join t_gw_co t2 on t1.co_cd = t2.co_cd                                                                                                                        ");
        sb.append(" left outer join t_gw_gds t3 on t1.gd_cd = t3.gd_cd                                                                                                                          ");

        JpaResultMapper jpaResult = new JpaResultMapper();
        Query query = em.createNativeQuery(sb.toString());

        if (StringUtil.isNotBlank(coCd)) {
            query.setParameter("coCd", coCd);
        }

        if (StringUtil.isNotBlank(gdCd)) {
            query.setParameter("gdCd", gdCd);
        }

        List<CustomerCoCdGdSeqSvcActDto> results = jpaResult.list(query, CustomerCoCdGdSeqSvcActDto.class);

        return results;
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCoOfrMbrRgstYmdDto> findAllGrpCoOfrMbrRgstYmd(Search search) {
        StringBuffer sb = new StringBuffer();
        StringBuffer sbSch = new StringBuffer();

        String coOfrMbrRgstYear = search.getString("coOfrMbrRgstYear");
        if (StringUtil.isNotBlank(coOfrMbrRgstYear)) {
            sbSch.append(" and substring(t1.cu_co_ofr_mbr_rgst_ymd, 1, 4) = :coOfrMbrRgstYear       ");
        }

        String coCd = search.getString("coCd");
        if (StringUtil.isNotBlank(coCd)) {
            sbSch.append(" and t1.co_cd = :coCd                                                             ");
        } else {
            sbSch.append(" and t1.co_cd in ('A00','A01','A10')                 ");
        }

        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            sbSch.append(" and t1.gd_cd = :gdCd                                                           ");
        }

        String coUseYn = search.getString("coUseYn");
        if (StringUtil.isNotBlank(coUseYn)) {
            sbSch.append(" and t1.cu_use_stop_yn = :coUseYn                       ");
        }

        String batchYn = search.getString("batchYn");
        if (StringUtil.isNotBlank(batchYn)) {
            sbSch.append(" and (case when 'SYSTEM' = t1.cu_rgstr_id then 'Y' else 'N' end) IN (:batchYn)                    ");
        }

        String tfaYn = search.getString("tfaYn");
        if (StringUtil.isNotBlank(tfaYn)) {
            sbSch.append(" and t2.ct_use_yn = :tfaYn                        ");
        }

        String cmTp = search.getString("cmTp");
        if (StringUtil.isNotBlank(cmTp)) {
            sbSch.append(" and t3.cm_tp = :cmTp                        ");
        }

        String coOfrMbrRgstStrtYmd = search.getString("coOfrMbrRgstStrtYmd");
        if (StringUtil.isNotBlank(coOfrMbrRgstStrtYmd)) {
            sbSch.append(" and t1.cu_co_ofr_mbr_rgst_ymd >= :coOfrMbrRgstStrtYmd                        ");
        }
        String coOfrMbrRgstEndYmd = search.getString("coOfrMbrRgstEndYmd");
        if (StringUtil.isNotBlank(coOfrMbrRgstStrtYmd)) {
            sbSch.append(" and t1.cu_co_ofr_mbr_rgst_ymd <= :coOfrMbrRgstEndYmd                        ");
        }

        sb.append(" select                                                                                                  ");
        sb.append("   t1.cu_co_ofr_mbr_rgst_ymd                                                                             ");
        sb.append("   , t1.co_cd                                                                                            ");
        sb.append("   , t1.gd_cd                                                                                           ");
        sb.append("   , t1.cu_rgstr_id                                                                                      ");
        sb.append("   , t1.cu_use_stop_yn                                                                                   ");
        sb.append("   , count(*) as cu_cnt                                                                                  ");
        sb.append(" from t_cu_cust t1                                                                                       ");
        sb.append(" left outer join t_cu_cust_tfa t2 on t1.cu_seq = t2.cu_seq                                               ");
        sb.append(" left outer join t_cu_cust_mapng t3 on t1.cu_seq = t3.cu_seq                                             ");
        sb.append(" where t1.cu_use_stop_yn = 'Y'        ");

        sb.append(sbSch.toString());

        sb.append(" group by t1.cu_co_ofr_mbr_rgst_ymd, t1.co_cd, t1.gd_cd, t1.cu_rgstr_id, t1.cu_use_stop_yn              ");

        JpaResultMapper jpaResult = new JpaResultMapper();

        Query query = em.createNativeQuery(sb.toString());

        if (StringUtil.isNotBlank(coOfrMbrRgstYear)) {
            query.setParameter("coOfrMbrRgstYear", coOfrMbrRgstYear);
        }
        if (StringUtil.isNotBlank(coCd)) {
            query.setParameter("coCd", coCd);
        }
        if (StringUtil.isNotBlank(gdCd)) {
            query.setParameter("gdCd", gdCd);
        }
        if (StringUtil.isNotBlank(coUseYn)) {
            query.setParameter("coUseYn", coUseYn);
        }
        if (StringUtil.isNotBlank(batchYn)) {
            query.setParameter("batchYn", batchYn);
        }
        if (StringUtil.isNotBlank(tfaYn)) {
            query.setParameter("tfaYn", tfaYn);
        }
        if (StringUtil.isNotBlank(cmTp)) {
            query.setParameter("cmTp", cmTp);
        }
        if (StringUtil.isNotBlank(coOfrMbrRgstStrtYmd)) {
            query.setParameter("coOfrMbrRgstStrtYmd", coOfrMbrRgstStrtYmd);
        }
        if (StringUtil.isNotBlank(coOfrMbrRgstEndYmd)) {
            query.setParameter("coOfrMbrRgstEndYmd", coOfrMbrRgstEndYmd);
        }

        List<CustomerCoOfrMbrRgstYmdDto> results = jpaResult.list(query, CustomerCoOfrMbrRgstYmdDto.class);

        return results;
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerBcTpTfaYnDto> findAllGrpBcTpTfaYn(Search search) {
        StringBuffer sb = new StringBuffer();
        StringBuffer sbSch1 = new StringBuffer();
        StringBuffer sbSch2 = new StringBuffer();

        String coCd = search.getString("coCd");
        if (StringUtil.isNotBlank(coCd)) {
            sbSch1.append(" and t1.co_cd = :coCd                       ");
        } else {
            sbSch1.append(" and t1.co_cd in ('A00','A01','A10')     ");
        }
        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            sbSch1.append(" and t1.gd_cd = :gdCd                      ");
        }
        String coOfrMbrRgstStrtYmd = search.getString("coOfrMbrRgstStrtYmd");
        if (StringUtil.isNotBlank(coOfrMbrRgstStrtYmd)) {
            sbSch1.append(" and t1.cu_co_ofr_mbr_rgst_ymd >= :coOfrMbrRgstStrtYmd                        ");
        }
        String coOfrMbrRgstEndYmd = search.getString("coOfrMbrRgstEndYmd");
        if (StringUtil.isNotBlank(coOfrMbrRgstStrtYmd)) {
            sbSch1.append(" and t1.cu_co_ofr_mbr_rgst_ymd <= :coOfrMbrRgstEndYmd                        ");
        }

        String coUseYn = search.getString("coUseYn");
        if (StringUtil.isNotBlank(coUseYn)) {
            sbSch2.append(" and t.cu_use_yn = :coUseYn                       ");
        }
        String batchYn = search.getString("batchYn");
        if (StringUtil.isNotBlank(batchYn)) {
            sbSch2.append(" and t.batch_yn = :batchYn                    ");
        }
        String tfaYn = search.getString("tfaYn");
        if (StringUtil.isNotBlank(tfaYn)) {
            sbSch2.append(" and t.tfa_yn = :tfaYn                        ");
        }
        String cmTp = search.getString("cmTp");
        if (StringUtil.isNotBlank(cmTp)) {
            sbSch2.append(" and t.cm_tp = :cmTp                         ");
        }

        sb.append("  select t.co_cd, t.gd_cd, t.cu_use_yn, t.batch_yn, t.tfa_yn, t.cm_tp, count(*)         ");
        sb.append("      from(                                                                              ");
        sb.append("          select t1.co_cd                                                                ");
        sb.append("               , t1.gd_cd                                                               ");
        sb.append("               , (case when 'Y' = t1.cu_use_stop_yn then 'Y' else 'N' end) as cu_use_yn  ");
        sb.append("               , (case when 'SYSTEM' = t1.cu_rgstr_id then 'Y' else 'N' end) as batch_yn ");
        sb.append("               , (case when 'Y' = t2.ct_use_yn then 'Y' else 'N' end) as tfa_yn          ");
        sb.append("               , isnull(t3.cm_tp,2) as cm_tp                                             ");
        sb.append("              from t_cu_cust t1                                                          ");
        sb.append("              left outer join t_cu_cust_tfa t2 on t1.cu_seq = t2.cu_seq                  ");
        sb.append("              left outer join t_cu_cust_mapng t3 on t1.cu_seq = t3.cu_seq                ");
        sb.append("              where 1=1                                                                  ");

        sb.append(sbSch1.toString());

        sb.append("          ) t        ");
        sb.append("          where 1=1  ");

        sb.append(sbSch2.toString());

        sb.append(" group by t.co_cd, t.gd_cd, t.cu_use_yn, t.batch_yn, t.tfa_yn, t.cm_tp ");
        sb.append(" order by t.co_cd, t.gd_cd                                             ");

        JpaResultMapper jpaResult = new JpaResultMapper();
        Query query = em.createNativeQuery(sb.toString());

        if (StringUtil.isNotBlank(coCd)) {
            query.setParameter("coCd", coCd);
        }
        if (StringUtil.isNotBlank(gdCd)) {
            query.setParameter("gdCd", gdCd);
        }
        if (StringUtil.isNotBlank(coUseYn)) {
            query.setParameter("coUseYn", coUseYn);
        }
        if (StringUtil.isNotBlank(batchYn)) {
            query.setParameter("batchYn", batchYn);
        }
        if (StringUtil.isNotBlank(tfaYn)) {
            query.setParameter("tfaYn", tfaYn);
        }
        if (StringUtil.isNotBlank(cmTp)) {
            query.setParameter("cmTp", cmTp);
        }
        if (StringUtil.isNotBlank(coOfrMbrRgstStrtYmd)) {
            query.setParameter("coOfrMbrRgstStrtYmd", coOfrMbrRgstStrtYmd);
        }
        if (StringUtil.isNotBlank(coOfrMbrRgstEndYmd)) {
            query.setParameter("coOfrMbrRgstEndYmd", coOfrMbrRgstEndYmd);
        }

        List<CustomerBcTpTfaYnDto> results = jpaResult.list(query, CustomerBcTpTfaYnDto.class);

        return results;
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCoCdGdSeqDto> findAllGrpCocdGdSeq(Search search) {
        StringBuffer qy = new StringBuffer();
        StringBuffer sbSch1 = new StringBuffer();

        String coCd = search.getString("coCd");
        if (StringUtil.isNotBlank(coCd)) {
            sbSch1.append(" and co_cd = :coCd                       ");
        }

        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            sbSch1.append(" and gd_cd = :gdCd                     ");
        }

        String setlObjYn = search.getString("setlObjYn");
        if (StringUtil.isNotBlank(setlObjYn)) {
            sbSch1.append(" and cu_setl_obj_yn = :setlObjYn            ");
        }

        String useStopYn = search.getString("useStopYn");
        if (StringUtil.isNotBlank(useStopYn)) {
            sbSch1.append(" and cu_use_stop_yn = :useStopYn            ");
        }

        qy.append("select                                                                                                               ");
        qy.append("            t1.co_cd                                                                                                 ");
        qy.append("          , t1.gd_cd                                                                                                ");
        qy.append("          , sum(case when t3.cu_seq is not null and t3.cu_use_stop_yn = 'Y' then t2.cnt else 0 end) as cnt1_1        ");
        qy.append("          , sum(case when t3.cu_seq is not null and t3.cu_use_stop_yn = 'N' then t2.cnt else 0 end) as cnt1_2        ");
        qy.append("          , sum(case when t3.cu_seq is not null then t2.cnt else 0 end) as cnt2_1                                    ");
        qy.append("          , sum(case when t3.cu_seq is null then t2.cnt  else 0 end) as cnt2_2                                       ");
        qy.append("          , sum(t2.cnt2_1) as cnt3_1                                                                                 ");
        qy.append("          , sum(t2.cnt2_2) as cnt3_2                                                                                 ");
        qy.append("     from                                                                                                            ");
        qy.append("          (                                                                                                          ");
        qy.append("              select                                                                                                 ");
        qy.append("                co_cd                                                                                                ");
        qy.append("                , gd_cd                                                                                             ");
        qy.append("              from t_cu_cust t                                                                                       ");
        qy.append("              where 1=1                                                                                              ");

        qy.append(sbSch1.toString());

        qy.append("              group by co_cd, gd_cd                                                                                 ");
        qy.append("          ) t1 inner join                                                                                            ");
        qy.append("          (                                                                                                          ");
        qy.append("              select                                                                                                 ");
        qy.append("                co_cd, gd_cd, cu_seq                                                                                ");
        qy.append("                , count(*) as cnt                                                                                    ");
        qy.append("                 , sum(case when cs_cust_tp != '0007' then 1 else 0 end) as cnt2_1                                   ");
        qy.append("                 , sum(case when cs_cust_tp = '0007' then 1 else 0 end) as cnt2_2                                    ");
        qy.append("              from t_cs_cnsl                                                                                         ");
        qy.append("              group by co_cd, gd_cd, cu_seq                                                                         ");
        qy.append("          ) t2 on t1.co_cd = t2.co_cd and t1.gd_cd = t2.gd_cd                                                      ");
        qy.append("              left outer join (                                                                                      ");
        qy.append("             select                                                                                                  ");
        qy.append("                    gd_cd                                                                                           ");
        qy.append("                    , cu_seq                                                                                         ");
        qy.append("                    , cu_use_stop_yn                                                                                 ");
        qy.append("             from t_cu_cust                                                                                          ");
        qy.append("             group by gd_cd, cu_seq, cu_use_stop_yn                                                                 ");
        qy.append("        ) t3 on t2.gd_cd = t3.gd_cd and t2.cu_seq = t3.cu_seq                                                      ");
        qy.append("    group by t1.co_cd, t1.gd_cd                                                                                     ");

        JpaResultMapper jpaResult = new JpaResultMapper();
        Query query = em.createNativeQuery(qy.toString());

        if (StringUtil.isNotBlank(coCd)) {
            query.setParameter("coCd", coCd);
        }
        if (StringUtil.isNotBlank(gdCd)) {
            query.setParameter("gdCd", gdCd);
        }
        if (StringUtil.isNotBlank(setlObjYn)) {
            query.setParameter("setlObjYn", setlObjYn);
        }
        if (StringUtil.isNotBlank(useStopYn)) {
            query.setParameter("useStopYn", useStopYn);
        }

        List<CustomerCoCdGdSeqDto> results = jpaResult.list(query, CustomerCoCdGdSeqDto.class);

        return results;
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCnslYmDto> findAllGrpCnslYm(Search search) {
        StringBuffer qy = new StringBuffer();
        StringBuffer schConds = new StringBuffer();

        String rgstYear = search.getString("rgstYear");
        if (StringUtil.isNotBlank(rgstYear)) {
            schConds.append(" and left(cu_rgst_ymd, 4) = :rgstYear ");
        }

        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            schConds.append(" and gd_cd = :gdCd ");
        }

        String useStopYn = search.getString("useStopYn");
        if (StringUtil.isNotBlank(useStopYn)) {
            schConds.append(" and cu_use_stop_yn = :useStopYn ");
        }

        String smsRcvRejctYn = search.getString("smsRcvRejctYn");
        if (StringUtil.isNotBlank(smsRcvRejctYn)) {
            schConds.append(" and cu_sms_rcv_rejct_yn = :smsRcvRejctYn ");
        }

        String emailRcvRejctYn = search.getString("emailRcvRejctYn");
        if (StringUtil.isNotBlank(emailRcvRejctYn)) {
            schConds.append(" and cu_email_rcv_rejct_yn = :emailRcvRejctYn ");
        }

        String telRcvRejctYn = search.getString("telRcvRejctYn");
        if (StringUtil.isNotBlank(telRcvRejctYn)) {
            schConds.append(" and cu_tel_rcv_rejct_yn = :telRcvRejctYn ");
        }

        String svcUseGuidLtrYn = search.getString("svcUseGuidLtrYn");
        if (StringUtil.isNotBlank(svcUseGuidLtrYn)) {
            schConds.append(" and cu_svc_use_guid_ltr_yn = :svcUseGuidLtrYn ");
        }

        String ageLo = search.getString("ageLo");
        String brtdy1 = search.getString("brtdy1");
        String sign1 = search.getString("sign1");
        String brtdy2 = search.getString("brtdy2");
        String sign2 = search.getString("sign2");

        if (StringUtil.isNotBlank(brtdy1) || StringUtil.isNotBlank(brtdy2)) {
            if (StringUtil.isNotBlank(sign1) || StringUtil.isNotBlank(sign2)) {
                schConds.append(" and ");
                schConds.append(this.getSchCondSign("cu_brtdy", "brtdy1", "brtdy2", ageLo, brtdy1, sign1, brtdy2, sign2));
            }
        }

        qy.append("\n select                                        ");
        qy.append("\n     rgstYm                                    ");
        qy.append("\n     , count(cu_cnsl_cnt) as cnt               ");
        qy.append("\n from                                          ");
        qy.append("\n     (                                         ");
        qy.append("\n         select                                ");
        qy.append("\n             left(cu_rgst_ymd, 6) as rgstYm    ");
        qy.append("\n              , cu_cnsl_cnt                    ");
        qy.append("\n         from t_cu_cust                        ");
        qy.append("\n         where 1=1                             ");
        qy.append("\n             and cu_cnsl_cnt <= 0              ");
        qy.append(schConds.toString());
        qy.append("\n     ) t                                       ");
        qy.append("\n group by t.rgstYm                             ");

        JpaResultMapper jpaResult = new JpaResultMapper();
        Query query = em.createNativeQuery(qy.toString());

        if (StringUtil.isNotBlank(rgstYear)) {
            query.setParameter("rgstYear", rgstYear);
        }
        if (StringUtil.isNotBlank(gdCd)) {
            query.setParameter("gdCd", gdCd);
        }
        if (StringUtil.isNotBlank(useStopYn)) {
            query.setParameter("useStopYn", useStopYn);
        }
        if (StringUtil.isNotBlank(smsRcvRejctYn)) {
            query.setParameter("smsRcvRejctYn", smsRcvRejctYn);
        }
        if (StringUtil.isNotBlank(emailRcvRejctYn)) {
            query.setParameter("emailRcvRejctYn", emailRcvRejctYn);
        }
        if (StringUtil.isNotBlank(telRcvRejctYn)) {
            query.setParameter("telRcvRejctYn", telRcvRejctYn);
        }
        if (StringUtil.isNotBlank(svcUseGuidLtrYn)) {
            query.setParameter("svcUseGuidLtrYn", svcUseGuidLtrYn);
        }
        if (StringUtil.isNotBlank(brtdy1) && StringUtil.isNotBlank(sign1)) {
            query.setParameter("brtdy1", brtdy1);
        }
        if (StringUtil.isNotBlank(brtdy2) && StringUtil.isNotBlank(sign2)) {
            query.setParameter("brtdy2", brtdy2);
        }

        List<CustomerCnslYmDto> results = jpaResult.list(query, CustomerCnslYmDto.class);

        return results;
    }
    
    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerSvcExpDto> findAllSvcExp(Search search) {
        StringBuffer qy = new StringBuffer();
        StringBuffer schConds = new StringBuffer();
        StringBuffer schConds2 = new StringBuffer();

        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            schConds.append(" and t1.gd_cd = :gdCd ");
        }

        String useStopYn = null;
        String svcPrdExp = null;
        String nm = null;
        String cph = null;
        String brtdy = null;
        String extnlCertiCd = null;

        String expChkYn = search.getString("expChkYn");
        if (StringUtil.equals(expChkYn, "Y")) {
            nm = search.getString("nm");
            if (StringUtil.isNotBlank(nm)) {
                schConds.append(" and t1.cu_nm = :nm ");
            }

            cph = search.getString("cph");
            if (StringUtil.isNotBlank(cph)) {
                cph = StringUtil.replace(cph, " ", "");
                cph = StringUtil.replace(cph, "-", "");
                if (StringUtil.length(cph) == 4) {
                    schConds.append(" and t1.cu_cph like :cph ");
                } else if (StringUtil.length(cph) > 4) {
                    schConds.append(" and t1.cu_cph = :cph ");
                }
            }

            brtdy = search.getString("brtdy");
            if (StringUtil.isNotBlank(brtdy)) {
                schConds.append(" and t1.cu_brtdy = :brtdy ");
            }
            extnlCertiCd = search.getString("extnlCertiCd");
            if (StringUtil.isNotBlank(extnlCertiCd)) {
                schConds.append(" and t1.cu_extnl_certi_cd = :extnlCertiCd ");
            }
            useStopYn = search.getString("useStopYn");
            if (StringUtil.isNotBlank(useStopYn)) {
                schConds.append(" and t1.cu_use_stop_yn = :useStopYn ");
            }
            svcPrdExp = search.getString("svcPrdExp");
            if (StringUtil.isNotBlank(svcPrdExp)) {
                schConds2.append(" and t.svc_prd_exp = :svcPrdExp");
            }
        }

        qy.append("\n select                             ");
        qy.append("\n     t.co_nm                        ");
        qy.append("\n      , t.co_cd                     ");
        qy.append("\n      , t.co_use_yn                 ");
        qy.append("\n      , t.gd_cd                    ");
        qy.append("\n      , t.gd_nm                     ");
        qy.append("\n      , t.gd_use_yn                 ");
        qy.append("\n      , t.gd_ctrt_use_yn            ");
        qy.append("\n      , t.gd_svc_ofr_prd            ");
        qy.append("\n      , t.cu_use_stop_yn            ");

        if (StringUtil.equals(expChkYn, "Y")) {
            qy.append("\n      , t.cu_co_ofr_mbr_rgst_ymd ");
            qy.append("\n      , t.svc_prd_exp            ");
            qy.append("\n      , t.svc_prd_chk            ");
        }

        qy.append("\n , count(*) as cnt ");

        if (StringUtil.equals(expChkYn, "N")) {
            qy.append("\n      , sum(case when 'Y' = t.svc_prd_exp then 1 else 0 end) as exp_y_cnt ");
            qy.append("\n      , sum(case when 'N' = t.svc_prd_exp then 1 else 0 end) as exp_n_cnt ");
        }

        qy.append("\n from (                                                                                        ");
        qy.append("\n          select t1.co_cd                                                                      ");
        qy.append("\n               , t1.gd_cd                                                                     ");
        qy.append("\n               , t1.cu_seq                                                                     ");
        qy.append("\n               , t1.cu_nm                                                                      ");
        qy.append("\n               , t1.cu_co_ofr_mbr_rgst_ymd                                                     ");
        qy.append("\n               , t1.cu_use_stop_yn                                                             ");
        qy.append("\n               , t1.cu_rgst_ymd                                                                ");
        qy.append("\n               , t2.co_nm                                                                      ");
        qy.append("\n               , t2.co_use_yn                                                                  ");
        qy.append("\n               , t2.gd_nm                                                                      ");
        qy.append("\n               , t2.gd_use_yn                                                                  ");
        qy.append("\n               , t2.gd_svc_ofr_prd                                                             ");
        qy.append("\n               , (case                                                                         ");
        qy.append("\n                      when t2.gd_svc_ofr_prd <                                                 ");
        qy.append("\n                           datediff(month, substring(t1.cu_co_ofr_mbr_rgst_ymd, 1, 4)          ");
        qy.append("\n                               + '-' + substring(t1.cu_co_ofr_mbr_rgst_ymd, 5, 2)              ");
        qy.append("\n                               + '-' + substring(t1.cu_co_ofr_mbr_rgst_ymd, 7, 2), getdate())  ");
        qy.append("\n                      then 'Y'                                                                 ");
        qy.append("\n                      else 'N' end) as svc_prd_exp                                             ");
        qy.append("\n               , datediff(month, substring(t1.cu_co_ofr_mbr_rgst_ymd, 1, 4)                    ");
        qy.append("\n              + '-' + substring(t1.cu_co_ofr_mbr_rgst_ymd, 5, 2)                               ");
        qy.append("\n              + '-' + substring(t1.cu_co_ofr_mbr_rgst_ymd, 7, 2), getdate()                    ");
        qy.append("\n              )                     as svc_prd_chk                                             ");
        qy.append("\n               , t2.gd_ctrt_use_yn                                                             ");
        qy.append("\n          from t_cu_cust t1                                                                    ");
        qy.append("\n                   inner join                                                                  ");
        qy.append("\n               (                                                                               ");
        qy.append("\n                   select a.co_cd                                                              ");
        qy.append("\n                        , a.co_nm                                                              ");
        qy.append("\n                        , a.co_use_yn                                                          ");
        qy.append("\n                        , b.gd_cd                                                             ");
        qy.append("\n                        , b.gd_nm                                                              ");
        qy.append("\n                        , b.gd_use_yn                                                          ");
        qy.append("\n                        , b.gd_svc_ofr_prd                                                     ");
        qy.append("\n                        , b.gd_ctrt_use_yn                                                     ");
        qy.append("\n                   from t_gw_co a                                                              ");
        qy.append("\n                            left outer join t_gw_gds b on a.co_cd = b.co_cd                    ");
        qy.append("\n               ) as t2 on t1.co_cd = t2.co_cd and t1.gd_cd = t2.gd_cd                        ");
        qy.append("\n          where 1=1                                                                            ");
        qy.append("\n            and t1.cu_co_ofr_mbr_rgst_ymd is not null                                          ");
        qy.append("\n            and len(t1.cu_co_ofr_mbr_rgst_ymd) = 8                                             ");
        qy.append(schConds.toString());
        qy.append("\n      ) t                                                                                      ");
        qy.append("\n      where 1=1                                                                                ");
        qy.append(schConds2.toString());
        qy.append("\n group by t.co_nm, t.co_cd, t.co_use_yn                                                        ");
        qy.append("\n , t.gd_cd, t.gd_nm, t.gd_use_yn, t.gd_ctrt_use_yn                                            ");
        qy.append("\n , t.cu_use_stop_yn, t.gd_svc_ofr_prd                                                          ");
        if (StringUtil.equals(expChkYn, "Y")) {
            qy.append("\n , t.cu_co_ofr_mbr_rgst_ymd, t.svc_prd_chk, t.svc_prd_exp ");
        }
        qy.append("\n order by t.co_nm, t.gd_nm ");
        if (StringUtil.equals(expChkYn, "N")) {
            qy.append("\n , t.gd_svc_ofr_prd ");
        }
        if (StringUtil.equals(expChkYn, "Y")) {
            qy.append("\n , t.cu_co_ofr_mbr_rgst_ymd ");
        }

        JpaResultMapper jpaResult = new JpaResultMapper();
        Query query = em.createNativeQuery(qy.toString());

        if (StringUtil.isNotBlank(gdCd)) {
            query.setParameter("gdCd", gdCd);
        }
        if (StringUtil.isNotBlank(nm)) {
            query.setParameter("nm", nm);
        }
        if (StringUtil.isNotBlank(cph)) {
            if (StringUtil.length(cph) == 4) {
                query.setParameter("cph", cph + "%");
            } else if (StringUtil.length(cph) > 4) {
                query.setParameter("cph", Aes256Util.fnEncryptT(cph));
            }
        }
        if (StringUtil.isNotBlank(brtdy)) {
            query.setParameter("brtdy", brtdy);
        }
        if (StringUtil.isNotBlank(extnlCertiCd)) {
            query.setParameter("extnlCertiCd", extnlCertiCd);
        }
        if (StringUtil.isNotBlank(useStopYn)) {
            query.setParameter("useStopYn", useStopYn);
        }
        if (StringUtil.isNotBlank(svcPrdExp)) {
            query.setParameter("svcPrdExp", svcPrdExp);
        }

        List<CustomerSvcExpDto> results = jpaResult.list(query, CustomerSvcExpDto.class);

        return results;
    }

    /**
     * <p>지정된 고객의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public Page<Customer> findPage(Search search) {
        Pageable pageable = search.getPageable();
        QueryResults<Customer> result = this.getQuery(search).fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    /**
     * <p>쿼리를 구합니다.</p>
     *
     * @param search 검색
     * @return 값
     */
    private JPQLQuery<Customer> getQuery(Search search) {
        JPQLQuery<Customer> query = queryFactory.selectFrom(customer);

        // 패치조인 query 추가
        String fthJnYn = search.getString("fthJnYn");
        if (StringUtil.equals("Y", fthJnYn)) {
            String fthJnEntt1 = search.getString("fthJnEntt1");
            if (StringUtil.equals("company", fthJnEntt1)) {
                query.leftJoin(customer.company).fetchJoin();
            }

            String fthJnEntt2 = search.getString("fthJnEntt2");
            if (StringUtil.equals("goods", fthJnEntt2)) {
                query.leftJoin(customer.goods).fetchJoin();
            }

            String fthJnEntt3 = search.getString("fthJnEntt3");
            if (StringUtil.equals("tfa", fthJnEntt3)) {
                query.leftJoin(customer.customerTfas).fetchJoin();
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
     * <p>쿼리를 구합니다.</p>
     *
     * @param search 검색
     * @return 값
     */
    public JPQLQuery<CustomerSetlObjYnDto> getQueryGrpBySetlObjYn(Search search) {
        JPQLQuery<Customer> query = this.getQuery(search);
        JPQLQuery<CustomerSetlObjYnDto> query1;

        query1 =
                query
                        .groupBy(customer.company.cd
                                , customer.goods.cd
                                , customer.setlObjYn
                                , customer.useStopYn
                        )
                        .select(Projections.constructor(CustomerSetlObjYnDto.class
                                , customer.company.cd
                                , customer.goods.cd
                                , customer.setlObjYn
                                , customer.useStopYn
                                , new CaseBuilder()
                                        .when(customer.setlObjYn.eq("Y").and(customer.useStopYn.eq("Y"))).then(1L).otherwise(0L).sum()
                                , new CaseBuilder()
                                        .when(customer.setlObjYn.eq("N").and(customer.useStopYn.eq("Y"))).then(1L).otherwise(0L).sum()
                                , new CaseBuilder()
                                        .when(customer.useStopYn.eq("Y")).then(1L).otherwise(0L).sum()
                                , new CaseBuilder()
                                        .when(customer.useStopYn.eq("N")).then(1L).otherwise(0L).sum()
                        ));

        return query1;
    }

    /**
     * <p>검색조건을 생성합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    private List<Predicate> getSchConds(Search search) {
        List<Predicate> schConds = new ArrayList<>();

        String seq = search.getString("seq");
        if (StringUtil.isNotBlank(seq)) {
            schConds.add(customer.seq.eq(Long.valueOf(seq)));
        }

        String nm = search.getString("nm");
        if (StringUtil.isNotBlank(nm)) {
            schConds.add(customer.nm.contains(nm));
        }

        String cph = search.getString("cph");
        if (StringUtil.isNotBlank(cph)) {
            cph = StringUtil.replace(cph, " ", "");
            cph = StringUtil.replace(cph, "-", "");
            if (StringUtil.length(cph) == 4) {
                schConds.add(customer.cph.startsWith(cph));
            } else {
                schConds.add(customer.cph.eq(cph));
            }
        }

        String brtdy = search.getString("brtdy");
        if (StringUtil.isNotBlank(brtdy)) {
            schConds.add(customer.brtdy.eq(brtdy));
        }

        String acnt = search.getString("acnt");
        if (StringUtil.isNotBlank(acnt)) {
            schConds.add(customer.acnt.eq(acnt));
        }

        String extnlCertiCd = search.getString("extnlCertiCd");
        if (StringUtil.isNotBlank(extnlCertiCd)) {
            schConds.add(customer.extnlCertiCd.eq(extnlCertiCd));
        }

        String coCd = search.getString("coCd");
        if (StringUtil.isNotBlank(coCd)) {
            schConds.add(customer.company.cd.eq(coCd));
        }

        String[] coCds = (String[]) search.get("coCds");
        if (coCds != null) {
            schConds.add(customer.company.cd.in(coCds));
        }

        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            schConds.add(customer.goods.cd.eq(gdCd));
        }

        String gdCdNot = search.getString("gdCdNot");
        if (StringUtil.isNotBlank(gdCdNot)) {
            schConds.add(customer.goods.cd.ne(gdCdNot));
        }

        String setlObjYn = search.getString("setlObjYn");
        if (StringUtil.isNotBlank(setlObjYn)) {
            schConds.add(customer.setlObjYn.eq(setlObjYn));
        }

        String useStopYn = search.getString("useStopYn");
        if (StringUtil.isNotBlank(useStopYn)) {
            schConds.add(customer.useStopYn.eq(useStopYn));
        }

        String smsRcvRejctYn = search.getString("smsRcvRejctYn");
        if (StringUtil.isNotBlank(smsRcvRejctYn)) {
            schConds.add(customer.smsRcvRejctYn.eq(smsRcvRejctYn));
        }

        String emailRcvRejctYn = search.getString("emailRcvRejctYn");
        if (StringUtil.isNotBlank(emailRcvRejctYn)) {
            schConds.add(customer.emailRcvRejctYn.eq(emailRcvRejctYn));
        }

        String telRcvRejctYn = search.getString("telRcvRejctYn");
        if (StringUtil.isNotBlank(telRcvRejctYn)) {
            schConds.add(customer.emailRcvRejctYn.eq(telRcvRejctYn));
        }

        String svcUseGuidLtrYn = search.getString("svcUseGuidLtrYn");
        if (StringUtil.isNotBlank(svcUseGuidLtrYn)) {
            schConds.add(customer.emailRcvRejctYn.eq(svcUseGuidLtrYn));
        }

        String coOfrMbrRgstYear = search.getString("coOfrMbrRgstYear");
        if (StringUtil.isNotBlank(coOfrMbrRgstYear)) {
            schConds.add(customer.coOfrMbrRgstYmd.substring(0, 4).eq(coOfrMbrRgstYear));
        }

        String rgstYm = search.getString("rgstYm");
        if (StringUtil.isNotBlank(rgstYm)) {
            schConds.add(customer.rgstYmd.substring(0, 6).eq(rgstYm));
        }

        String cnslYn = search.getString("cnslYn");
        if (StringUtil.isNotBlank(cnslYn)) {
            if (StringUtil.equals(cnslYn, "Y")) {
                schConds.add(customer.cnslCnt.gt(0));
            } else {
                schConds.add(customer.cnslCnt.loe(0));
            }
        }

        String ageLo = search.getString("ageLo");
        String brtdy1 = search.getString("brtdy1");
        String sign1 = search.getString("sign1");
        String brtdy2 = search.getString("brtdy2");
        String sign2 = search.getString("sign2");

        if (StringUtil.isNotBlank(brtdy1) || StringUtil.isNotBlank(brtdy2)) {
            addSchConds(schConds, customer.brtdy, ageLo, brtdy1, sign1, brtdy2, sign2);
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
        List<OrderSpecifier> orderSpecifiers = search.getQdslSortConds(customer);

        if (orderSpecifiers.size() == 0) {
            orderSpecifiers.add(Search.getQdslSortedColumn(Order.DESC, customer, "seq"));
        }

        return orderSpecifiers;
    }

    /**
     * <p>정렬조건을 생성합니다.</p>
     *
     * @param schConds 검색조건
     * @param path 경로
     * @param lo 연산자
     * @param var1 변수1
     * @param sign1 부호1
     * @param var2 변수2
     * @param sign2 부호2
     */
    private void addSchConds(List<Predicate> schConds, StringPath path, String lo, String var1, String sign1, String var2, String sign2) {
        if (StringUtil.isNotBlank(var1, var2)) {
            if (StringUtil.equals(lo, "OR")) {
                BooleanExpression be1 = this.getBooleanExpression(sign1, path, var1);
                BooleanExpression be2 = this.getBooleanExpression(sign2, path, var2);
                schConds.add(be1.or(be2));
            } else {
                schConds.add(this.getBooleanExpression(sign1, path, var1));
                schConds.add(this.getBooleanExpression(sign2, path, var2));
            }
            return;
        }

        if (StringUtil.isNotBlank(var1)) {
            schConds.add(this.getBooleanExpression(sign1, path, var1));
            return;
        }

        if (StringUtil.isNotBlank(var2)) {
            schConds.add(this.getBooleanExpression(sign2, path, var2));
            return;
        }

        return;
    }

    private BooleanExpression getBooleanExpression(String sign, StringPath path, String var) {
        BooleanExpression be = null;

        if (StringUtil.isNotBlank(sign)) {
            switch (sign) {
                case "GT":
                    be = path.gt(var);
                    break;
                case "GE":
                    be = path.goe(var);
                    break;
                case "EQ":
                    be = path.eq(var);
                    break;
                case "LE":
                    be = path.loe(var);
                    break;
                case "LT":
                    be = path.lt(var);
                    break;
                default:
                    break;
            }
        }

        return be;
    }

    private String getSchCondSign(String col, String colNm1, String colNm2, String lo, String var1, String sign1, String var2, String sign2) {
        StringBuffer sb = new StringBuffer();

        if (StringUtil.isNotBlank(var1)) {
            sb.append(col);
            sb.append(" ");
            sb.append(this.getSign(sign1));
            sb.append(" ");
            sb.append(" :");
            sb.append(colNm1);

            if (StringUtil.isNotBlank(var2)) {
                sb.append(" ");
                sb.append(lo);
                sb.append(" ");
            }
        }

        if (StringUtil.isNotBlank(var2)) {
            sb.append(col);
            sb.append(" ");
            sb.append(this.getSign(sign1));
            sb.append(" ");
            sb.append(" :");
            sb.append(colNm2);
        }

        return sb.toString();
    }

    private String getSign(String sign) {
        String result = null;

        if (StringUtil.isNotBlank(sign)) {
            switch (sign) {
                case "GT":
                    result = ">";
                    break;
                case "GE":
                    result = ">=";
                    break;
                case "EQ":
                    result = "=";
                    break;
                case "LE":
                    result = "<=";
                    break;
                case "LT":
                    result = "<";
                    break;
                default:
                    break;
            }
        }

        return result;
    }

}