package com.aaihc.crm.biz.log.service;

import com.aaihc.crm.biz.log.domain.CustomerCumulationBatch;
import com.aaihc.crm.biz.log.domain.CustomerCumulationBatchGrpRnkDto;
import com.aaihc.crm.biz.log.repository.CustomerCumulationBatchRepository;
import com.aaihc.crm.core.domain.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCumulationBatchServiceImpl (고객 누적 배치 Service Implement)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 11.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
@RequiredArgsConstructor
public class CustomerCumulationBatchServiceImpl implements CustomerCumulationBatchService {

    private final CustomerCumulationBatchRepository customerCumulationBatchRepository;

    /**
     * <p>고객 누적 배치을 등록합니다</p>
     *
     * @see com.aaihc.crm.biz.log.service.CustomerCumulationBatchService#add(CustomerCumulationBatch)
     * @param customerCumulationBatch 고객 누적 배치
     * @return 수
     */
    public long add(CustomerCumulationBatch customerCumulationBatch) {
        long result = 0;

        customerCumulationBatchRepository.save(customerCumulationBatch);

        result = customerCumulationBatch.getSeq();

        return result;
    }

    /**
     * <p>지정된 고객 누적 배치을 수정합니다</p>
     *
     * @see com.aaihc.crm.biz.log.service.CustomerCumulationBatchService#modify(CustomerCumulationBatch customerCumulationBatch)
     * @param customerCumulationBatch 고객 누적 배치
     * @return 수
     */
    public long modify(CustomerCumulationBatch customerCumulationBatch) {
        long result = 0;

        CustomerCumulationBatch orgCustomerCumulationBatch = this.findOne(customerCumulationBatch.getSeq());
        if (orgCustomerCumulationBatch != null) {
            result = 1;
        }

        return result;
    }

    /**
     * <p>지정된 고객 누적 배치을 삭제합니다</p>
     *
     * @see com.aaihc.crm.biz.log.service.CustomerCumulationBatchService#remove(long)
     * @param seq 일련번호
     * @return 수
     */
    public long remove(long seq) {
        long result = 0;
        if (seq > 0) {
            customerCumulationBatchRepository.deleteById(seq);
            result = 1;
        }

        return result;
    }

    /**
     * <p>지정된 고객 누적 배치을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.log.service.CustomerCumulationBatchService#findOne(long)
     * @param seq 일련번호
     * @return 고객 누적 배치
     */
    @Transactional(readOnly = true)
    public CustomerCumulationBatch findOne(long seq) {
        return customerCumulationBatchRepository.findById(seq);
    }

    /**
     * <p>지정된 고객 누적 배치의 수를 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.log.service.CustomerCumulationBatchService#findTotalCnt(Search)
     * @param search 검색
     * @return 수
     */
    @Transactional(readOnly = true)
    public long findTotalCnt(Search search) {
        return customerCumulationBatchRepository.count(search);
    }

    /**
     * <p>고객 누적 배치의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.log.service.CustomerCumulationBatchService#findList(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerCumulationBatch> findList(Search search) {
        return customerCumulationBatchRepository.findAll(search);
    }

    /**
     * <p>지정된 고객 누적 배치의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerCumulationBatchGrpRnkDto> findListGrpRnk1(Search search) {
        return customerCumulationBatchRepository.findAllGrpRnk1(search);
    }

    /**
     * <p>고객 누적 배치의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.log.service.CustomerCumulationBatchService#findPage(Search)
     * @param search 검색
     * @return 페이징 목록
     */
    @Transactional(readOnly = true)
    public Page<CustomerCumulationBatch> findPage(Search search) {
        return customerCumulationBatchRepository.findPage(search);
    }

    /**
     * <p>고객 누적 배치의 목록을 가져옵니다 (패치조인)</p>
     *
     * @see CustomerCumulationBatchService#findFJoinList(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerCumulationBatch> findFJoinList(Search search) {
        search.add("fthJnYn", "Y");
        search.add("fthJnEntt1", "company");
        search.add("fthJnEntt2", "goods");
        return this.findList(search);
    }

    /**
     * <p>고객 누적 배치의 목록을 가져옵니다 (패치조인)</p>
     *
     * @see CustomerCumulationBatchService#findFJoinPage(Search)
     * @param search 검색
     * @return 페이징 목록
     */
    @Transactional(readOnly = true)
    public Page<CustomerCumulationBatch> findFJoinPage(Search search) {
        search.add("fthJnYn", "Y");
        search.add("fthJnEntt1", "company");
        search.add("fthJnEntt2", "goods");
        return this.findPage(search);
    }
}