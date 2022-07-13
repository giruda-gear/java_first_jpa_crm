package com.aaihc.crm.core.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>Search (Search Domain)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 06. 01.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SheetStatics {

    private String nm; // 시트이름
    private Map hdrs; // 헤더
    private Map<Integer, List<String>> colNms; // 열이름
    private Map<Integer, List<List<String>>> contRows; // 내용
    private List<CellRangeAddress> mergedCells; // 셀병합

}
