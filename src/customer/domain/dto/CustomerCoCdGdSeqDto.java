package com.aaihc.crm.biz.customer.domain.dto;

import asn.util.lang.StringUtil;
import com.aaihc.crm.biz.groundwork.domain.Company;
import com.aaihc.crm.biz.groundwork.domain.Goods;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCoCdGdSeqDto (고객 Dto)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 29.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerCoCdGdSeqDto {
    private static final long serialVersionUID = 1L;

    private String coCd;
    private String gdCd;
    private Integer cnt11;
    private Integer cnt12;
    private Integer cnt21;
    private Integer cnt22;
    private Integer cnt31;
    private Integer cnt32;

    @QueryProjection
    public CustomerCoCdGdSeqDto(String coCd, String gdCd, Integer cnt11, Integer cnt12, Integer cnt21, Integer cnt22, Integer cnt31, Integer cnt32) {
        this.coCd = coCd;
        this.gdCd = gdCd;
        this.cnt11 = cnt11;
        this.cnt12 = cnt12;
        this.cnt21 = cnt21;
        this.cnt22 = cnt22;
        this.cnt31 = cnt31;
        this.cnt32 = cnt32;
    }

    public String[] getCoNm(List<Company> companies) {
        String[] results = new String[2];
        Company company;

        if (companies != null) {
            Optional<Company> optional = companies.stream().filter(p -> StringUtil.equals(p.getCd(), coCd)).findFirst();
            if (optional.isPresent()) {
                company = optional.get();

                if (StringUtil.equals(company.getUseYn(), "Y")) {
                    results[0] = "[제휴]";
                } else {
                    results[0] = "[종료]";
                }

                results[1] = company.getNm();
            }
        }

        return results;
    }

    public String[] getGdNm(List<Goods> goodses) {
        String[] results = new String[2];
        StringBuffer sbPrefix = new StringBuffer();
        Goods goods;

        if (goodses != null) {
            Optional<Goods> optional = goodses.stream().filter(p -> StringUtil.equals(gdCd, p.getCd())).findFirst();
            if (optional.isPresent()) {
                goods = optional.get();

                if (StringUtil.equals(goods.getCtrtUseYn(), "Y")) {
                    sbPrefix.append("[계약 유지]");
                } else {
                    sbPrefix.append("[계약 종료]");
                }

                if (StringUtil.equals(goods.getUseYn(), "Y")) {
                    sbPrefix.append("[서비스 진행]");
                } else {
                    sbPrefix.append("[서비스 종료]");
                }

                results[0] = sbPrefix.toString();
                results[1] = goods.getNm();
            }
        }

        return results;
    }

//    public static long getSumTotCnt(List<CustomerCoCdGdSeqDto> customerBcTpTfaYnDtos) {
//        long sum = customerBcTpTfaYnDtos.stream().mapToLong(p -> p.getCnt()).sum();
//        return sum;
//    }

}