package com.aaihc.crm.biz.customer.domain.dto;

import asn.util.lang.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCnslYmDto (고객 Dto)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 04. 29.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerCnslYmDto {
    private static final long serialVersionUID = 1L;

    private String ym;
    private Integer cnt;

    List<CustomerCnslYmDto> customerCnslYmDtos;

    public CustomerCnslYmDto(String ym, Integer cnt) {
        this.ym = ym;
        this.cnt = cnt;
    }

    public CustomerCnslYmDto(List<CustomerCnslYmDto> customerCnslYmDtos) {
        this.customerCnslYmDtos = customerCnslYmDtos;
    }

    public long getCntByMonth(String month) {
        long result = 0;
        Optional<CustomerCnslYmDto> optional = customerCnslYmDtos.stream().filter(p -> StringUtil.equals(StringUtil.right(p.getYm(), 2), month)).findFirst();
        if (optional.isPresent()) {
            result = optional.get().getCnt();
        }

        return result;
    }
}