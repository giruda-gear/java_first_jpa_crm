package com.aaihc.crm.biz.customer.service;

import asn.util.date.DateFormatUtil;
import asn.util.date.DateUtil;
import asn.util.lang.StringUtil;
import com.aaihc.crm.biz.customer.domain.Customer;
import com.aaihc.crm.biz.customer.domain.CustomerIndividualHistory;
import com.aaihc.crm.biz.customer.domain.dto.*;
import com.aaihc.crm.biz.customer.repository.CustomerRepository;
import com.aaihc.crm.biz.customer.validation.CustomerValidator;
import com.aaihc.crm.core.domain.BaseDomain;
import com.aaihc.crm.core.domain.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerServiceImpl (고객 Service Implement)</p>
 *
 * @author 	    : 양용수
 * date 		: 2021. 03. 18.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerValidator customerValidator;

    private final CustomerIndividualHistoryService customerIndividualHistoryService;

    /**
     * <p>고객을 등록합니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#add(Customer)
     * @param customer 고객
     * @return 수
     */
    public long add(Customer customer) {
        long result;

        customerValidator.add(customer);
        customerRepository.save(customer);

        result = customer.getSeq();

        return result;
    }

    /**
     * <p>지정된 고객을 수정합니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#modify(Customer customer)
     * @param customer 고객
     * @return 수
     */
    public long modify(Customer customer) {
        long result = 0;

        Customer orgCustomer = this.findOne(customer.getSeq());

        customerValidator.modify(orgCustomer, customer);

        if (orgCustomer != null) {
            List<CustomerIndividualHistory> customerIndividualHistories = CustomerIndividualHistory.of("Y", orgCustomer, customer);

            orgCustomer.setGdCd(customer.getGdCd());
            orgCustomer.setNm(customer.getNm());
            orgCustomer.setBrtdy(customer.getBrtdy());
            orgCustomer.setSex(customer.getSex());

            orgCustomer.setSvcTel(customer.getSvcTel());
            orgCustomer.setTel(customer.getTel());

            orgCustomer.setEmail(customer.getEmail());
            orgCustomer.setExtnlCertiCd(customer.getExtnlCertiCd());

            orgCustomer.setZipcd(customer.getZipcd());
            orgCustomer.setBaseAddr(customer.getBaseAddr());
            orgCustomer.setDtlAddr(customer.getDtlAddr());

            orgCustomer.setSetlObjYn(customer.getSetlObjYn());

            orgCustomer.setUseStopYn(customer.getUseStopYn());
            orgCustomer.setSmsRcvRejctYn(customer.getSmsRcvRejctYn());
            orgCustomer.setEmailRcvRejctYn(customer.getEmailRcvRejctYn());
            orgCustomer.setTelRcvRejctYn(customer.getTelRcvRejctYn());

            orgCustomer.setAgreeYn1(customer.getAgreeYn1());
            orgCustomer.setAgreeYn2(customer.getAgreeYn2());
            orgCustomer.setAgreeYn3(customer.getAgreeYn3());

            orgCustomer.setMgTp(customer.getMgTp());
            orgCustomer.setCvcptTdcTp(customer.getCvcptTdcTp());
            orgCustomer.setExclsMgrTp(customer.getExclsMgrTp());

            orgCustomer.setPculr(customer.getPculr());

            orgCustomer.setHtnd1Itm1Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm1Ymdt(), customer.getHtnd1Itm1Yn()));
            orgCustomer.setHtnd1Itm2Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm2Ymdt(), customer.getHtnd1Itm2Yn()));
            orgCustomer.setHtnd1Itm3Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm3Ymdt(), customer.getHtnd1Itm3Yn()));
            orgCustomer.setHtnd1Itm4Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm4Ymdt(), customer.getHtnd1Itm4Yn()));
            orgCustomer.setHtnd1Itm5Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm5Ymdt(), customer.getHtnd1Itm5Yn()));
            orgCustomer.setHtnd1Itm6Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm6Ymdt(), customer.getHtnd1Itm6Yn()));
            orgCustomer.setHtnd1Itm6(customer.getHtnd1Itm6());

            orgCustomer.setHtnd2Itm1Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm1Ymdt(), customer.getHtnd2Itm1Yn()));
            orgCustomer.setHtnd2Itm2Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm2Ymdt(), customer.getHtnd2Itm2Yn()));
            orgCustomer.setHtnd2Itm3Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm3Ymdt(), customer.getHtnd2Itm3Yn()));
            orgCustomer.setHtnd2Itm4Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm4Ymdt(), customer.getHtnd2Itm4Yn()));
            orgCustomer.setHtnd2Itm5Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm5Ymdt(), customer.getHtnd2Itm5Yn()));
            orgCustomer.setHtnd2Itm6Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm6Ymdt(), customer.getHtnd2Itm6Yn()));
            orgCustomer.setHtnd2Itm6(customer.getHtnd2Itm6());

            orgCustomer.setHtnd3Itm1Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm1Ymdt(), customer.getHtnd3Itm1Yn()));
            orgCustomer.setHtnd3Itm2Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm2Ymdt(), customer.getHtnd3Itm2Yn()));
            orgCustomer.setHtnd3Itm3Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm3Ymdt(), customer.getHtnd3Itm3Yn()));
            orgCustomer.setHtnd3Itm4Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm4Ymdt(), customer.getHtnd3Itm4Yn()));
            orgCustomer.setHtnd3Itm5Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm5Ymdt(), customer.getHtnd3Itm5Yn()));
            orgCustomer.setHtnd3Itm6Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm6Ymdt(), customer.getHtnd3Itm6Yn()));
            orgCustomer.setHtnd3Itm6(customer.getHtnd3Itm6());

            orgCustomer.setHtnd4Memo(customer.getHtnd4Memo());

            orgCustomer.setModr(customer.getModr());
            orgCustomer.setModYmd(BaseDomain.getCurrYmd());
            orgCustomer.setModHis(BaseDomain.getCurrHis());

            // 고객 개인정보 이력
            if (customerIndividualHistories.size() > 0) {
                customerIndividualHistoryService.add(customerIndividualHistories);
            }

            result = 1;
        }

        return result;
    }

    /**
     * <p>지정된 고객을 수정합니다 (상담용)</p>
     *
     * @param customer 고객
     * @return 수
     */
    public long modifyForCnsl(Customer customer) {
        long result = 0;

        Customer orgCustomer = this.findOne(customer.getSeq());

        customerValidator.modify(orgCustomer, customer);

        if (orgCustomer != null) {
            List<CustomerIndividualHistory> customerIndividualHistories = CustomerIndividualHistory.of("N", orgCustomer, customer);

            orgCustomer.setSvcTel(customer.getSvcTel());
            orgCustomer.setTel(customer.getTel());

            orgCustomer.setBaseAddr(customer.getBaseAddr());
            orgCustomer.setDtlAddr(customer.getDtlAddr());
            orgCustomer.setZipcd(customer.getZipcd());
            orgCustomer.setEmail(customer.getEmail());

            orgCustomer.setUseStopYn(customer.getUseStopYn());
            orgCustomer.setSmsRcvRejctYn(customer.getSmsRcvRejctYn());
            orgCustomer.setEmailRcvRejctYn(customer.getEmailRcvRejctYn());
            orgCustomer.setTelRcvRejctYn(customer.getTelRcvRejctYn());

            orgCustomer.setAgreeYn1(customer.getAgreeYn1());
            orgCustomer.setAgreeYn2(customer.getAgreeYn2());
            orgCustomer.setAgreeYn3(customer.getAgreeYn3());

            orgCustomer.setMgTp(customer.getMgTp());
            orgCustomer.setCvcptTdcTp(customer.getCvcptTdcTp());
            orgCustomer.setExclsMgrTp(customer.getExclsMgrTp());

            orgCustomer.setPculr(customer.getPculr());

            orgCustomer.setHtnd1Itm1Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm1Ymdt(), customer.getHtnd1Itm1Yn()));
            orgCustomer.setHtnd1Itm2Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm2Ymdt(), customer.getHtnd1Itm2Yn()));
            orgCustomer.setHtnd1Itm3Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm3Ymdt(), customer.getHtnd1Itm3Yn()));
            orgCustomer.setHtnd1Itm4Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm4Ymdt(), customer.getHtnd1Itm4Yn()));
            orgCustomer.setHtnd1Itm5Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm5Ymdt(), customer.getHtnd1Itm5Yn()));
            orgCustomer.setHtnd1Itm6Ymdt(this.getHtndYmdt(orgCustomer.getHtnd1Itm6Ymdt(), customer.getHtnd1Itm6Yn()));
            orgCustomer.setHtnd1Itm6(customer.getHtnd1Itm6());

            orgCustomer.setHtnd2Itm1Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm1Ymdt(), customer.getHtnd2Itm1Yn()));
            orgCustomer.setHtnd2Itm2Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm2Ymdt(), customer.getHtnd2Itm2Yn()));
            orgCustomer.setHtnd2Itm3Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm3Ymdt(), customer.getHtnd2Itm3Yn()));
            orgCustomer.setHtnd2Itm4Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm4Ymdt(), customer.getHtnd2Itm4Yn()));
            orgCustomer.setHtnd2Itm5Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm5Ymdt(), customer.getHtnd2Itm5Yn()));
            orgCustomer.setHtnd2Itm6Ymdt(this.getHtndYmdt(orgCustomer.getHtnd2Itm6Ymdt(), customer.getHtnd2Itm6Yn()));
            orgCustomer.setHtnd2Itm6(customer.getHtnd2Itm6());

            orgCustomer.setHtnd3Itm1Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm1Ymdt(), customer.getHtnd3Itm1Yn()));
            orgCustomer.setHtnd3Itm2Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm2Ymdt(), customer.getHtnd3Itm2Yn()));
            orgCustomer.setHtnd3Itm3Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm3Ymdt(), customer.getHtnd3Itm3Yn()));
            orgCustomer.setHtnd3Itm4Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm4Ymdt(), customer.getHtnd3Itm4Yn()));
            orgCustomer.setHtnd3Itm5Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm5Ymdt(), customer.getHtnd3Itm5Yn()));
            orgCustomer.setHtnd3Itm6Ymdt(this.getHtndYmdt(orgCustomer.getHtnd3Itm6Ymdt(), customer.getHtnd3Itm6Yn()));
            orgCustomer.setHtnd3Itm6(customer.getHtnd3Itm6());

            orgCustomer.setHtnd4Memo(customer.getHtnd4Memo());

            // 고객 개인정보 이력
            if (customerIndividualHistories.size() > 0) {
                customerIndividualHistoryService.add(customerIndividualHistories);
            }
        }

        result = 1;

        return result;
    }

    private String getHtndYmdt(String orgHtndYmdt, String htndYn) {
        if (StringUtil.isBlank(orgHtndYmdt) && StringUtil.equals(htndYn, "Y")) {
            return DateFormatUtil.format(new Date(), "yyyyMMddHHmmss");
        }

        if (StringUtil.isNotBlank(orgHtndYmdt) && !StringUtil.equals(htndYn, "Y")) {
            return null;
        }

        return orgHtndYmdt;
    }

    /**
     * <p>지정된 고객의 소멸여부를 수정합니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#modifyForExtncYn(long, String)
     * @param seq 일련번호
     * @param extncYn 소멸여부
     * @return 수
     */
    public long modifyForExtncYn(long seq, String extncYn) {
        Customer orgCustomer = customerRepository.findById(seq);
        orgCustomer.setExtncYn(extncYn);
        orgCustomer.setExtncYmd(DateUtil.getCurrentDate());
        orgCustomer.setExtncHis(DateFormatUtil.format(new Date(), "HHmmss"));
        return 1;
    }

    /**
     * <p>지정된 고객을 삭제합니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#remove(long)
     * @param seq 일련번호
     * @return 수
     */
    public long remove(long seq) {
        long result = 0;
        if (seq > 0) {
            customerRepository.deleteById(seq);
            result = 1;
        }

        return result;
    }

    /**
     * <p>지정된 고객을 삭제합니다 (소멸)</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#removeByExtncYn(String)
     * @param ymd 년월일
     */
    public void removeByExtncYn(String ymd) {
        customerRepository.deleteByExtncYn(ymd);
    }

    /**
     * <p>지정된 고객을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findOne(long)
     * @param seq 일련번호
     * @return 고객
     */
    @Transactional(readOnly = true)
    public Customer findOne(long seq) {
        return customerRepository.findById(seq);
    }

    /**
     * <p>지정된 고객의 수를 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findTotalCnt(Search)
     * @param search 검색
     * @return 수
     */
    @Transactional(readOnly = true)
    public long findTotalCnt(Search search) {
        return customerRepository.count(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findList(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<Customer> findList(Search search) {
        return customerRepository.findAll(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findListGrpCoCdGdSeqSvcAct(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerCoCdGdSeqSvcActDto> findListGrpCoCdGdSeqSvcAct(Search search) {
        return customerRepository.findAllGrpCoCdGdSeqSvcAct(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findListGrpCoOfrMbrRgstYmd(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerCoOfrMbrRgstYmdDto> findListGrpCoOfrMbrRgstYmd(Search search) {
        return customerRepository.findAllGrpCoOfrMbrRgstYmd(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findListGrpBcTpTfaYn(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerBcTpTfaYnDto> findListGrpBcTpTfaYn(Search search) {
        return customerRepository.findAllGrpBcTpTfaYn(search);
    }

    /**
     * <p>지정된 고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findListGrpCocdGdSeq(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerCoCdGdSeqDto> findListGrpCocdGdSeq(Search search) {
        return customerRepository.findAllGrpCocdGdSeq(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findListGrpSetlObjYn(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerSetlObjYnDto> findListGrpSetlObjYn(Search search) {
        return customerRepository.findAllGrpBySetlObjYn(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findListSvcAnonymousAssistanceCase(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerSvcAnonymousAssistanceCaseDto> findListSvcAnonymousAssistanceCase(Search search) {
        return customerRepository.findAllsvcAnonymousAssistanceCase(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findListGrpCnslYm(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerCnslYmDto> findListGrpCnslYm(Search search) {
        return customerRepository.findAllGrpCnslYm(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findListSvcExp(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<CustomerSvcExpDto> findListSvcExp(Search search) {
        return customerRepository.findAllSvcExp(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findPage(Search)
     * @param search 검색
     * @return 페이징 목록
     */
    @Transactional(readOnly = true)
    public Page<Customer> findPage(Search search) {
        return customerRepository.findPage(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다 (패치조인)</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findFJoinList(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<Customer> findFJoinList(Search search) {
        search.add("fthJnYn", "Y");
        search.add("fthJnEntt1", "company");
        search.add("fthJnEntt2", "goods");
        return customerRepository.findAll(search);
    }

    /**
     * <p>고객의 목록을 가져옵니다 (페치조인)</p>
     *
     * @see com.aaihc.crm.biz.customer.service.CustomerService#findFJoinPage(Search)
     * @param search 검색
     * @return 페이징 목록
     */
    @Transactional(readOnly = true)
    public Page<Customer> findFJoinPage(Search search) {
        search.add("fthJnYn", "Y");
        search.add("fthJnEntt1", "company");
        search.add("fthJnEntt2", "goods");
        return customerRepository.findPage(search);
    }

}