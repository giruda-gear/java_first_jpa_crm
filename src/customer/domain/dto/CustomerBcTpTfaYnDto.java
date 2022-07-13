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
 * <p>CustomerBcTpTfaYnDto (고객 Dto)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 18.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerBcTpTfaYnDto {
    private static final long serialVersionUID = 1L;

    private String coCd;
    private String gdCd;
    private String useYn;
    private String batchYn;
    private String tfaYn;
    private Integer cmTp;
    private Integer cnt;

    @QueryProjection
    public CustomerBcTpTfaYnDto(String coCd, String gdCd, String useYn, String batchYn, String tfaYn, Integer cmTp, Integer cnt) {
        this.coCd = coCd;
        this.gdCd = gdCd;
        this.useYn = useYn;
        this.batchYn = batchYn;
        this.tfaYn = tfaYn;
        this.cmTp = cmTp;
        this.cnt = cnt;
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

    public static long getSumTotCnt(List<CustomerBcTpTfaYnDto> customerBcTpTfaYnDtos) {
        long sum = customerBcTpTfaYnDtos.stream().mapToLong(p -> p.getCnt()).sum();
        return sum;
    }

}