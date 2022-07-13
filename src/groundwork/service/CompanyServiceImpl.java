package com.aaihc.crm.biz.groundwork.service;

import asn.util.lang.StringUtil;
import com.aaihc.crm.biz.groundwork.domain.Company;
import com.aaihc.crm.biz.groundwork.repository.CompanyRepository;
import com.aaihc.crm.biz.groundwork.validation.CompanyValidator;
import com.aaihc.crm.core.domain.Search;
import com.querydsl.core.types.Order;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
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
 * <p>CompanyServiceImpl (고객사 Service Implement)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 08.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private CompanyValidator companyValidator;

    @Autowired
    public void setCompanyValidator(CompanyValidator companyValidator) {
        this.companyValidator = companyValidator;
    }

    /**
     * <p>고객사을 등록합니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.CompanyService#add(Company)
     * @param company 고객사
     * @return 수
     */
    public String add(Company company) {
        String result = "";

        // 유효성 확인
        companyValidator.add(company);
        company.setCd(StringUtil.upperCase(company.getCd()));

        companyRepository.save(company);
        result = company.getCd();

        return result;
    }

    /**
     * <p>지정된 고객사을 수정합니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.CompanyService#save(String, Company)
     * @param orgCd 코드
     * @param company 고객사
     * @return 수
     */
    public long save(String orgCd, Company company) {
        long result = 0;
        Company orgCompany = this.findOne(orgCd);
        if (orgCompany != null) {
            this.remove(orgCd);
            this.add(company);
            result = 1;
        }

        return result;
    }

    /**
     * <p>지정된 고객사을 삭제합니다</p>
     *
     * @see CompanyService#remove(String)
     * @param cd 일련번호
     * @return 수
     */
    public long remove(String cd) {
        long result = 0;

        // 유효성 확인
        companyValidator.remove(cd);

        companyRepository.deleteById(cd);
        result = 1;

        return result;
    }

    /**
     * <p>지정된 고객사을 가져옵니다</p>
     *
     * @see CompanyService#findOne(String)
     * @param cd 일련번호
     * @return 고객사
     */
    @Transactional(readOnly = true)
    public Company findOne(String cd) {
        return companyRepository.findById(cd);
    }

    /**
     * <p>지정된 고객사의 수를 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.CompanyService#findTotalCnt(Search)
     * @param search 검색
     * @return 수
     */
    @Transactional(readOnly = true)
    public long findTotalCnt(Search search) {
        return companyRepository.count(search);
    }

    /**
     * <p>고객사의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.CompanyService#findList(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<Company> findList(Search search) {
        return companyRepository.findAll(search);
    }

    /**
     * <p>고객사의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.CompanyService#findPage(Search)
     * @param search 검색
     * @return 페이징 목록
     */
    @Transactional(readOnly = true)
    public Page<Company> findPage(Search search) {
        return companyRepository.findPage(search);
    }

    /**
     * <p>고객사의 목록을 맵 형태로 가져옵니다</p>
     *
     * @param useYn 사용여부
     * @return 목록
     */
    public ListOrderedMap findMap(String useYn) {
        Search search = new Search();

        if (StringUtil.isNotBlank(useYn)) {
            search.add("useYn", useYn);
        }

        search.add("sortFld", "nm");
        search.add("sortMetd", Order.ASC);

        return Company.getMap((this.findList(search)));
    }

}