package com.aaihc.crm.biz.customer.domain.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerSvcAnonymousAssistanceCaseDto (고객 Dto)</p>
 *
 * @author 	    : 김현준
 * date 		: 2021. 03. 18.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerSvcAnonymousAssistanceCaseDto {
    private String cuNm;
    private String coCd;
    private String coNm;
    private String gdCd;
    private String gdNm;
    private Integer cfSeq;
    private Integer csSeq;
    private String rgstrNm;
    private String csRgstYmd;
    private String csRgstHis;
    private String cfCase;
    private String cfNat;
    private String cfCity;
    private String cfTel;
    private String cfCabkNo;
    private String cfCabkEmail;
    private int cnt;

    private List<CustomerSvcAnonymousAssistanceCaseDto> customerSvcAnonymousAssistanceCaseDtos;

    public CustomerSvcAnonymousAssistanceCaseDto(String cuNm, String coCd, String coNm, String gdCd, String gdNm, Integer cfSeq, Integer csSeq, String rgstrNm, String csRgstYmd, String csRgstHis, String cfCase, String cfNat, String cfCity, String cfTel, String cfCabkNo, String cfCabkEmail, int cnt) {
        this.cuNm = cuNm;
        this.coCd = coCd;
        this.coNm = coNm;
        this.gdCd = gdCd;
        this.gdNm = gdNm;
        this.cfSeq = cfSeq;
        this.csSeq = csSeq;
        this.rgstrNm = rgstrNm;
        this.csRgstYmd = csRgstYmd;
        this.csRgstHis = csRgstHis;
        this.cfCase = cfCase;
        this.cfNat = cfNat;
        this.cfCity = cfCity;
        this.cfTel = cfTel;
        this.cfCabkNo = cfCabkNo;
        this.cfCabkEmail = cfCabkEmail;
        this.cnt = cnt;
    }

    public CustomerSvcAnonymousAssistanceCaseDto(List<CustomerSvcAnonymousAssistanceCaseDto> customerSvcAnonymousAssistanceCaseDtos) {
        this.customerSvcAnonymousAssistanceCaseDtos = customerSvcAnonymousAssistanceCaseDtos;
    }
}
