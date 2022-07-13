package com.aaihc.crm.biz.log.domain;

import asn.util.lang.StringUtil;
import asn.util.num.NumberUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.map.ListOrderedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCumulationBatchGrpRnkDto (고객 누적 배치 (파일 사이클, 고객사, 상품, 년월 최신1건) Dto Domain)</p>
 *
 * @author : 양용수
 * date 		: 2021. 03. 11.
 * <p>
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerCumulationBatchGrpRnkDto {
    private static final long serialVersionUID = 1L;

    private String fileCyc;
    private int totCnt;
    private String custTretYmd;
    private String coCd;
    private String gdCd;
    private String coNm;
    private Character coUseYn;
    private String gdNm;
    private Character gdCtrtUseYn;
    private Character gdUseYn;

    private List<CustomerCumulationBatchGrpRnkDto> customerCumulationBatchGrpRnkDtos = new ArrayList<>();

    public CustomerCumulationBatchGrpRnkDto(String fileCyc, int totCnt, String custTretYmd, String coCd, String gdCd, String coNm, Character coUseYn, String gdNm, Character gdCtrtUseYn, Character gdUseYn) {
        this.fileCyc = fileCyc;
        this.totCnt = totCnt;
        this.custTretYmd = custTretYmd;
        this.coCd = coCd;
        this.gdCd = gdCd;
        this.coNm = coNm;
        this.coUseYn = coUseYn;
        this.gdNm = gdNm;
        this.gdCtrtUseYn = gdCtrtUseYn;
        this.gdUseYn = gdUseYn;
    }

    public CustomerCumulationBatchGrpRnkDto(List<CustomerCumulationBatchGrpRnkDto> customerCumulationBatchGrpRnkDtos) {
        this.customerCumulationBatchGrpRnkDtos = customerCumulationBatchGrpRnkDtos;
    }

    public String getGdNm() {
        if (StringUtil.isBlank(gdNm)) {
            return "전체";
        }

        return gdNm;
    }

    public String getGdCtrtUseYnNotNull() {
        String result = StringUtil.toString(this.getGdCtrtUseYn());
        if (StringUtil.isBlank(result)) {
            result = StringUtil.toString(getCoUseYn()); // NULL일 경우 고객사 사용여부
        }

        return result;
    }

    public boolean equals_(String fileCyc, String coCd, String gdCd, String ym) {
        if (!StringUtil.equals(fileCyc, this.fileCyc)) {
            return false;
        }

        if (!StringUtil.equals(coCd, this.coCd)) {
            return false;
        }

        if (gdCd != null && this.gdCd != null) {
            if (!gdCd.equals(this.gdCd)) {
                return false;
            }
        }

        if (!StringUtil.equals(ym, StringUtil.left(this.custTretYmd, 6))) {
            return false;
        }

        return true;
    }

    public List<CustomerCumulationBatchGrpRnkDto> getUniqueCustomerCumulationBatchGrpRnkDtos()  {
        List<CustomerCumulationBatchGrpRnkDto> results = new ArrayList<>();

        Map<String, List<CustomerCumulationBatchGrpRnkDto>> groupBy = customerCumulationBatchGrpRnkDtos.stream()
                .collect(Collectors.groupingBy(p -> (p.getGrpNm()), ListOrderedMap::new, Collectors.toList()));
        for (Map.Entry<String, List<CustomerCumulationBatchGrpRnkDto>> entry : groupBy.entrySet()) {
            String key = entry.getKey();

            CustomerCumulationBatchGrpRnkDto customerCumulationBatchGrpRnkDto_ = entry.getValue().get(0);

            String[] keys = StringUtil.split(key, "|");

            CustomerCumulationBatchGrpRnkDto customerCumulationBatchGrpRnkDto = new CustomerCumulationBatchGrpRnkDto();
            customerCumulationBatchGrpRnkDto.setCoNm(keys[0]);
            customerCumulationBatchGrpRnkDto.setCoCd(keys[1]);
            customerCumulationBatchGrpRnkDto.setGdNm(keys[2]);
            customerCumulationBatchGrpRnkDto.setGdCd(keys[3]);
            customerCumulationBatchGrpRnkDto.setFileCyc(keys[4]);

            customerCumulationBatchGrpRnkDto.setCoUseYn(customerCumulationBatchGrpRnkDto_.getCoUseYn());
            customerCumulationBatchGrpRnkDto.setGdCtrtUseYn(customerCumulationBatchGrpRnkDto_.getGdCtrtUseYn());
            customerCumulationBatchGrpRnkDto.setGdUseYn(customerCumulationBatchGrpRnkDto_.getGdUseYn());

            results.add(customerCumulationBatchGrpRnkDto);
        }

        return results;
    }

    private String getGrpNm() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getCoNm());
        sb.append("|");
        sb.append(this.getCoCd());
        sb.append("|");
        sb.append(this.getGdNm());
        sb.append("|");
        sb.append(this.getGdCd());
        sb.append("|");
        sb.append(this.getFileCyc());

        return sb.toString();
    }

    public String getCoNmPrefix() {
        String prefix;
        if (StringUtil.equals(this.getCoUseYn().toString(), "Y")) {
            prefix = "[제휴]";
        } else {
            prefix = "[종료]";
        }

        return prefix;
    }

    public String getGdNmPrefix() {
        StringBuffer sbPrefix = new StringBuffer();
        String gdCtrtUseYn = this.getGdCtrtUseYnNotNull();
        String gdUseYn = StringUtil.toString(this.getGdUseYn());

        if (StringUtil.equals(gdCtrtUseYn, "Y")) {
            sbPrefix.append("[계약 유지]");
        } else {
            sbPrefix.append("[계약 종료]");
        }

        if (StringUtil.equals(gdUseYn, "Y")) {
            sbPrefix.append("[서비스 진행]");
        } else if (StringUtil.equals(gdUseYn, "N")) {
            sbPrefix.append("[서비스 종료]");
        }

        return sbPrefix.toString();
    }

    public long getSumTotCnt(String fileCyc, String coCd, String gdCd, String ym) {
        long sum = customerCumulationBatchGrpRnkDtos.stream().filter(p -> p.equals_(fileCyc, coCd, gdCd, ym)).mapToLong(p -> p.getTotCnt()).sum();
        return sum;
    }

    public long getSumTotCnt(String ym) {
        long sum = customerCumulationBatchGrpRnkDtos.stream().filter(p -> StringUtil.equals(StringUtil.left(p.custTretYmd, 6), ym)).mapToLong(p -> p.getTotCnt()).sum();
        return sum;
    }

}