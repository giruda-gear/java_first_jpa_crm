package com.aaihc.crm.biz.customer.domain.dto;

import asn.util.lang.StringUtil;
import asn.util.num.NumberUtil;
import com.aaihc.crm.biz.groundwork.domain.Company;
import com.aaihc.crm.biz.groundwork.domain.Goods;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCoOfrMbrRgstYmdDto (고객 Dto)</p>
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
public class CustomerCoOfrMbrRgstYmdDto {
    private static final long serialVersionUID = 1L;

    private String coOfrMbrRgstYmd;
    private String coCd;
    private String gdCd;
    private String rgstrId;
    private Character useYn;
    private Integer cnt;

    private List<CustomerCoOfrMbrRgstYmdDto> customerCoOfrMbrRgstYmdDtos;

    @QueryProjection
    public CustomerCoOfrMbrRgstYmdDto(String coOfrMbrRgstYmd, String coCd, String gdCd, String rgstrId, Character useYn, Integer cnt) {
        this.coOfrMbrRgstYmd = coOfrMbrRgstYmd;
        this.coCd = coCd;
        this.gdCd = gdCd;
        this.rgstrId = rgstrId;
        this.useYn = useYn;
        this.cnt = cnt;
    }

    public CustomerCoOfrMbrRgstYmdDto(List<CustomerCoOfrMbrRgstYmdDto> customerCoOfrMbrRgstYmdDtos) {
        this.customerCoOfrMbrRgstYmdDtos = customerCoOfrMbrRgstYmdDtos;
    }

    public boolean equals_(String coCd, String gdCd, String ym) {
        if (!StringUtil.equals(coCd, this.coCd)) {
            return false;
        }

        if (gdCd != null && this.gdCd != null) {
            if (!gdCd.equals(this.gdCd)) {
                return false;
            }
        }

        if (!StringUtil.equals(ym, StringUtil.left(this.coOfrMbrRgstYmd, 6))) {
            return false;
        }

        return true;
    }

    public List<CustomerCoOfrMbrRgstYmdDto> getUniqueCustomerCoOfrMbrRgstYmdDtos()  {
        List<CustomerCoOfrMbrRgstYmdDto> results = new ArrayList<>();

        Map<String, List<CustomerCoOfrMbrRgstYmdDto>> groupBy = customerCoOfrMbrRgstYmdDtos.stream()
                .collect(Collectors.groupingBy(p -> (p.getGrpNm()), TreeMap::new, Collectors.toList()));
        for (Map.Entry<String, List<CustomerCoOfrMbrRgstYmdDto>> entry : groupBy.entrySet()) {
            String key = entry.getKey();

            String[] keys = StringUtil.split(key, "|");

            CustomerCoOfrMbrRgstYmdDto customerCoOfrMbrRgstYmdDto = new CustomerCoOfrMbrRgstYmdDto();
            customerCoOfrMbrRgstYmdDto.setCoCd(keys[0]);
            customerCoOfrMbrRgstYmdDto.setGdCd(keys[1]);

            results.add(customerCoOfrMbrRgstYmdDto);
        }

        return results;
    }

    private String getGrpNm() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getCoCd());
        sb.append("|");
        sb.append(this.getGdCd());

        return sb.toString();
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

    public long getSumTotCnt(String coCd, String gdCd, String month) {
        long sum = customerCoOfrMbrRgstYmdDtos.stream().filter(p -> p.equals_(coCd, gdCd, month)).mapToLong(p -> p.getCnt()).sum();
        return sum;
    }

    public long getSumTotCnt(String ym) {
        long sum = customerCoOfrMbrRgstYmdDtos.stream().filter(p -> StringUtil.equals(StringUtil.left(p.getCoOfrMbrRgstYmd(), 6), ym)).mapToLong(p -> p.getCnt()).sum();
        return sum;
    }

}