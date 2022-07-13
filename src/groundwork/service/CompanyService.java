package com.aaihc.crm.biz.groundwork.service;

import com.aaihc.crm.biz.groundwork.domain.Company;
import com.aaihc.crm.core.domain.Search;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>Company Service (고객사 Service)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 08.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
public interface CompanyService {

    /**
     * <p>고객사을 등록합니다</p>
     *
     * @param company 고객사
     * @return 수
     */
    public String add(Company company);

    /**
     * <p>지정된 고객사을 저장합니다</p>
     *
     * @param orgCd 코드
     * @param company 고객사
     * @return 수
     */
    public long save(String orgCd, Company company);

    /**
     * <p>지정된 고객사을 삭제합니다</p>
     *
     * @param cd 코드
     * @return 수
     */
    public long remove(String cd);

    /**
     * <p>지정된 고객사을 가져옵니다</p>
     *
     * @param cd 코드
     * @return 고객사
     */
    public Company findOne(String cd);

    /**
     * <p>지정된 고객사의 수를 가져옵니다</p>
     *
     * @param search 검색
     * @return 수
     */
    public long findTotalCnt(Search search);

    /**
     * <p>지정된 고객사의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 목록
     */
    public List<Company> findList(Search search);

    /**
     * <p>고객사의 목록을 가져옵니다</p>
     *
     * @param search 검색
     * @return 페이징 목록
     */
    public Page<Company> findPage(Search search);

    /**
     * <p>고객사의 목록을 맵 형태로 가져옵니다</p>
     *
     * @param useYn 사용여부
     * @return 목록
     */
    public ListOrderedMap findMap(String useYn);

}