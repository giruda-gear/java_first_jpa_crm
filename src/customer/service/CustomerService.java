package com.aaihc.crm.biz.customer.service;

import com.aaihc.crm.biz.customer.domain.Customer;
import com.aaihc.crm.biz.customer.domain.dto.*;
import com.aaihc.crm.core.domain.Search;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>Customer Service (고객 Service)</p>
 *
 * @author 	    : 양용수
 * date 		: 2021. 03. 18.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
public interface CustomerService {

    /**
     * <p>고객을 등록합니다</p>
     *
     * @param customer 고객
     * @return 수
     */
    public long add(Customer customer);

    /**
     * <p>지정된 고객을 수정합니다</p>
     *
     * @param customer 고객
     * @return 수
     */
    public long modify(Customer customer);

    /**
     * <p>지정된 고객을 수정합니다</p>
     *
     * @param customer 고객
     * @return 수
     */
    public long modifyForCnsl(Customer customer);

    /**
     * <p>지정된 고객의 소멸여부를 수정합니다</p>
     *
     * @param seq 일련번호
     * @param extncYn 소멸여부
     * @return 수
     */
    public long modifyForExtncYn(long seq, String extncYn);

    /**
     * <p>지정된 고객을 삭제합니다</p>
     *
     * @param seq 일련번호
     * @return 수
     */
    public long remove(long seq);

    /**
     * <p>지정된 고객을 삭제합니다 (소멸)</p>
     *
     * @param ymd 년월일
     */
    public void removeByExtncYn(String ymd);

    /**
     * <p>지정된 고객을 가져옵니다</p>
     *
     * @param seq 일련번호
     * @return 고객
     */
    public Customer findOne(long seq);

    /**
     * <p>고객의 수를 가져옵니다</p>
     *
     * @param search 검색
     * @return 수
     */
    public long findTotalCnt(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<Customer> findList(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCoCdGdSeqSvcActDto> findListGrpCoCdGdSeqSvcAct(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCoOfrMbrRgstYmdDto> findListGrpCoOfrMbrRgstYmd(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerBcTpTfaYnDto> findListGrpBcTpTfaYn(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerSetlObjYnDto> findListGrpSetlObjYn(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCoCdGdSeqDto> findListGrpCocdGdSeq(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerSvcAnonymousAssistanceCaseDto> findListSvcAnonymousAssistanceCase(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerCnslYmDto> findListGrpCnslYm(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<CustomerSvcExpDto> findListSvcExp(Search search);

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 페이징 목록
     */
    public Page<Customer> findPage(Search search);

    /**
     * <p>고객의 목록을 가져옵니다 (패치조인)</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<Customer> findFJoinList(Search search);

    /**
     * <p>고객의 목록을 가져옵니다 (페치조인)</p>
     *
     * @param search 검색
     * @return 페이징 목록
     */
    public Page<Customer> findFJoinPage(Search search);

}