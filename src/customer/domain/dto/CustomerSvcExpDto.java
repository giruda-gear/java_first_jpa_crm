package com.aaihc.crm.biz.customer.domain.dto;

import asn.util.lang.StringUtil;
import asn.util.num.NumberUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerSvcExpDto (고객 Dto)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 05. 04.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerSvcExpDto {
    private static final long serialVersionUID = 1L;

    private String coNm;
    private String coCd;
    private Character coUseYn;
    private String gdCd;
    private String gdNm;
    private Character gdUseYn;
    private Character gdCtrtUseYn;
    private String gdSvcOfrPrd;
    private Character cuUseStopYn;

    private String coOfrMbrRgstYmd;
    private String svcPrdExp;
    private Integer svcPrdChk;
    private Integer cnt;

    private Integer expYCnt;
    private Integer expNCnt;

    private List<CustomerSvcExpDto> customerSvcExpDtos;


    public CustomerSvcExpDto(String coNm, String coCd, Character coUseYn, String gdCd, String gdNm, Character gdUseYn, Character gdCtrtUseYn, String gdSvcOfrPrd, Character cuUseStopYn, Integer cnt, Integer expYCnt, Integer expNCnt) {
        this.coNm = coNm;
        this.coCd = coCd;
        this.coUseYn = coUseYn;
        this.gdCd = gdCd;
        this.gdNm = gdNm;
        this.gdUseYn = gdUseYn;
        this.gdCtrtUseYn = gdCtrtUseYn;
        this.gdSvcOfrPrd = gdSvcOfrPrd;
        this.cuUseStopYn = cuUseStopYn;
        this.cnt = cnt;
        this.expYCnt = expYCnt;
        this.expNCnt = expNCnt;
    }

    public CustomerSvcExpDto(String coNm, String coCd, Character coUseYn, String gdCd, String gdNm, Character gdUseYn, Character gdCtrtUseYn, String gdSvcOfrPrd, Character cuUseStopYn, String coOfrMbrRgstYmd, String svcPrdExp, Integer svcPrdChk, Integer cnt) {
        this.coNm = coNm;
        this.coCd = coCd;
        this.coUseYn = coUseYn;
        this.gdCd = gdCd;
        this.gdNm = gdNm;
        this.gdUseYn = gdUseYn;
        this.gdCtrtUseYn = gdCtrtUseYn;
        this.gdSvcOfrPrd = gdSvcOfrPrd;
        this.cuUseStopYn = cuUseStopYn;
        this.coOfrMbrRgstYmd = coOfrMbrRgstYmd;
        this.svcPrdExp = svcPrdExp;
        this.svcPrdChk = svcPrdChk;
        this.cnt = cnt;
    }

    public CustomerSvcExpDto(List<CustomerSvcExpDto> customerSvcExpDtos) {
        this.customerSvcExpDtos = customerSvcExpDtos;
    }

    public long getTotCnt(String tp) {
        long result = 0;

        if (StringUtil.equals(tp, "Y")) {
            result = this.getCustomerSvcExpDtos().stream().mapToLong(p -> p.getExpYCnt()).sum();
            return result;
        }
        if (StringUtil.equals(tp, "N")) {
            result = this.getCustomerSvcExpDtos().stream().mapToLong(p -> p.getExpNCnt()).sum();
            return result;
        }
        if (StringUtil.equals(tp, "T")) {
            result = this.getCustomerSvcExpDtos().stream().mapToLong(p -> p.getCnt()).sum();
            return result;
        }

        return result;
    }

    public double getPrdPcnt() {
        double result = NumberUtil.round(this.getSvcPrdChk() / NumberUtil.toDouble(this.getGdSvcOfrPrd()) * 100, 1);
        return result;
    }

    public int getEffectPrd() {
        int result = NumberUtil.toInt(this.getGdSvcOfrPrd()) - this.getSvcPrdChk();
        return result;
    }

    public int getAbsPrd(int val) {
        return Math.abs(val);
    }

}