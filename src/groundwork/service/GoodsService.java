package com.aaihc.crm.biz.groundwork.service;

import com.aaihc.crm.biz.groundwork.domain.Goods;
import com.aaihc.crm.core.domain.Search;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>Goods Service (상품 Service)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 11.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
public interface GoodsService {

    /**
     * <p>상품을 등록합니다</p>
     *
     * @param goods 상품
     * @return 수
     */
    public String add(Goods goods);

    /**
     * <p>지정된 상품을 수정합니다</p>
     *
     * @param goods 상품
     * @return 수
     */
    public long modify(Goods goods);

    /**
     * <p>지정된 상품을 삭제합니다</p>
     *
     * @param cd 코드
     * @return 수
     */
    public long remove(String cd);

    /**
     * <p>지정된 상품을 가져옵니다</p>
     *
     * @param cd 코드
     * @return 상품
     */
    public Goods findOne(String cd);

    /**
     * <p>지정된 상품의 수를 가져옵니다</p>
     *
     * @param search 검색
     * @return 수
     */
    public long findTotalCnt(Search search);

    /**
     * <p>지정된 상품의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<Goods> findList(Search search);

    /**
     * <p>상품의 목록을 가져옵니다 (페치조인)</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<Goods> findFJoinList(Search search);

    /**
     * <p>상품의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 페이징 목록
     */
    public Page<Goods> findPage(Search search);

    /**
     * <p>상품의 목록을 가져옵니다 (페치조인)</p>
     *
     * @param search 검색
     * @return 목록
     */
    public Page<Goods> findFJoinPage(Search search);

    /**
     * <p>상품의 목록을 맵 형태로 가져옵니다</p>
     *
     * @param useYn 사용여부
     * @return 목록
     */
    public ListOrderedMap findMap(String useYn);

}