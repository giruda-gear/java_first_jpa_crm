package com.aaihc.crm.biz.groundwork.repository;

import asn.util.lang.StringUtil;
import com.aaihc.crm.biz.groundwork.domain.GoodsService;
import com.aaihc.crm.core.domain.Search;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.aaihc.crm.biz.groundwork.domain.QGoodsService.goodsService;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>GoodsServiceRepository (상품 서비스 Repository)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 11.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Repository
public class GoodsServiceRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public GoodsServiceRepository(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(GoodsService.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * <p>상품 서비스을 등록합니다</p>
     *
     * @param goodsService 상품 서비스
     */
    public void save(GoodsService goodsService) {
        em.persist(goodsService);
    }

    /**
     * <p>지정된 상품 서비스을 조회합니다.</p>
     *
     * @param seq 일련번호
     * @return Optional GoodsService
     */
    public GoodsService findById(long seq) {
        return em.find(GoodsService.class, seq);
    }

    /**
     * <p>지정된 상품 서비스을 삭제합니다.</p>
     *
     * @param seq 일련번호
     * @return 수
     */
    public int deleteById(long seq) {
        int result = 0;

        if (seq > 0) {
            GoodsService goodsService = this.findById(seq);
            if (goodsService != null) {
                em.remove(goodsService);
                result = 1;
            }
        }

        return result;
    }

    /**
     * <p>지정된 상품 서비스의 개수를 조회합니다.</p>
     *
     * @param search 검색
     * @return 수
     */
    public long count(Search search) {
        return this.getQuery(search).fetchCount();
    }

    /**
     * <p>지정된 상품 서비스의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<GoodsService> findAll(Search search) {
        return this.getQuery(search).fetch();
    }

    /**
     * <p>지정된 상품 서비스의 목록을 조회합니다.</p>
     *
     * @param search 검색
     * @return 목록
     */
    public Page<GoodsService> findPage(Search search) {
        Pageable pageable = search.getPageable();
        QueryResults<GoodsService> result = this.getQuery(search).fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    /**
     * <p>쿼리를 구합니다.</p>
     *
     * @param search 검색
     * @return 값
     */
    private JPQLQuery<GoodsService> getQuery(Search search) {
        JPQLQuery<GoodsService> query = queryFactory.selectFrom(goodsService);

        // 패치조인 query 추가
        String fthJnYn = search.getString("fthJnYn");
        if (StringUtil.equals("Y", fthJnYn)) {
            String fthJnEntt1 = search.getString("fthJnEntt1");
            if (StringUtil.equals("service", fthJnEntt1)) {
                query.leftJoin(goodsService.service).fetchJoin();
            }

            String fthJnEntt2 = search.getString("fthJnEntt2");
            if (StringUtil.equals("serviceDetail", fthJnEntt2)) {
                query.leftJoin(goodsService.serviceDetail).fetchJoin();
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
                case "svcNm" :
                    schConds.add(goodsService.service.nm.containsIgnoreCase(schTxt));
                    break;
                case "svcDtl" :
                    schConds.add(goodsService.serviceDetail.dtl.containsIgnoreCase(schTxt));
                    break;
                default:
                    break;
            }
        }

        Object seq = search.get("seq");
        if (seq != null) {
            schConds.add(goodsService.seq.eq((Long) seq));
        }

        String gdCd = search.getString("gdCd");
        if (StringUtil.isNotBlank(gdCd)) {
            schConds.add(goodsService.gdCd.eq(gdCd));
        }

        Object svSeq = search.get("svSeq");
        if (svSeq != null) {
            schConds.add(goodsService.svSeq.eq((Long) svSeq));
        }

        Object sdSeq = search.get("sdSeq");
        if (sdSeq != null) {
            schConds.add(goodsService.sdSeq.eq((Long) sdSeq));
        }

        Object sdSeqs = search.get("sdSeqs");
        if (sdSeqs != null) {
            schConds.add(goodsService.sdSeq.in((CollectionExpression<?, ? extends Long>) sdSeqs));
        }

        String rstctCntNotNull = search.getString("rstctCntNotNull");
        if (StringUtil.equals(rstctCntNotNull, "Y")) {
            schConds.add(goodsService.rstctCnt.isNotNull());
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
        List<OrderSpecifier> orderSpecifiers = search.getQdslSortConds(goodsService);

        if (orderSpecifiers.size() == 0) {
            orderSpecifiers.add(Search.getQdslSortedColumn(Order.DESC, goodsService, "seq"));
        }

        return orderSpecifiers;
    }

}