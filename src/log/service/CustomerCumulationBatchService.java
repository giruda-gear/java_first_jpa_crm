package com.aaihc.crm.biz.log.service;

import com.aaihc.crm.biz.log.domain.CustomerCumulationBatch;
import com.aaihc.crm.biz.log.domain.CustomerCumulationBatchGrpRnkDto;
import com.aaihc.crm.core.domain.Search;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCumulationBatch Service (고객 누적 배치 Service)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 11.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
public interface CustomerCumulationBatchService {

    /**
     * <p>고객 누적 배치을 등록합니다</p>
     *
     * @param customerCumulationBatch 고객 누적 배치
     * @return 수
     */
    public long add(CustomerCumulationBatch customerCumulationBatch);

    /**
     * <p>지정된 고객 누적 배치을 수정합니다</p>
     *
     * @param customerCumulationBatch 고객 누적 배치
     * @return 수
     */
    public long modify(CustomerCumulationBatch customerCumulationBatch);

    /**
     * <p>지정된 고객 누적 배치을 삭제합니다</p>
     *
     * @param seq 일련번호
     * @return 수
     */
    public long remove(long seq);

    /**
     * <p>지정된 고객 누적 배치을 가져옵니다</p>
     *
     * @param seq 일련번호
     * @return 고객 누적 배치
     */
    public CustomerCumulationBatch findOne(long seq);

    /**
     * <p>지정된 고객 누적 배치의 수를 가져옵니다</p>
     *
     * @param search 검색
     * @return 수
     */
    public long findTotalCnt(Search search);

    /**
     * <p>지정된 고객 누적 배치의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCumulationBatch> findList(Search search);

    /**
     * <p>지정된 고객 누적 배치의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCumulationBatchGrpRnkDto> findListGrpRnk1(Search search);

    /**
     * <p>고객 누적 배치의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 페이징 목록
     */
    public Page<CustomerCumulationBatch> findPage(Search search);

    /**
     * <p>지정된 고객 누적 배치의 목록을 가져옵니다 (패치조인)</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCumulationBatch> findFJoinList(Search search);

    /**
     * <p>고객 누적 배치의 목록을 가져옵니다 (패치조인)</p>
     *
     * @param search 검색
     * @return 페이징 목록
     */
    public Page<CustomerCumulationBatch> findFJoinPage(Search search);    

}