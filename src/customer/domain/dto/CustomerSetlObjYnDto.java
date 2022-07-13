package com.aaihc.crm.biz.customer.domain.dto;

import asn.util.lang.StringUtil;
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
 * <p>CustomerSetlObjYnDto (고객 Dto)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 25.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerSetlObjYnDto {
    private String coCd;
    private String coNm;
    private String gdCd;
    private String gdNm;

    private String setlObjYn;
    private String useStopYn;
    private long setlObjYCnt;
    private long setlObjNCnt;
    private long setlObjYnTotCnt;
    private long useStopYCnt;
    private long useStopNCnt;
    private long useStopYnTotCnt;

    private long cnt11;
    private long cnt12;
    private long cnt21;
    private long cnt22;
    private long cnt31;
    private long cnt32;

    private List<Company> companies;
    private List<Goods> goodses;
    private List<CustomerSetlObjYnDto> customerSetlObjYnDtos;
    private List<CustomerCoCdGdSeqDto> customerCoCdGdSeqDtos;

    @QueryProjection
    public CustomerSetlObjYnDto(String coCd, String gdCd, String setlObjYn, String useStopYn, long setlObjYCnt, long setlObjNCnt, long useStopYCnt, long useStopNCnt) {
        this.coCd = coCd;
        this.gdCd = gdCd;
        this.setlObjYn = setlObjYn;
        this.useStopYn = useStopYn;
        this.setlObjYCnt = setlObjYCnt;
        this.setlObjNCnt = setlObjNCnt;
        this.useStopYCnt = useStopYCnt;
        this.useStopNCnt = useStopNCnt;
    }

    public CustomerSetlObjYnDto(List<Company> companies, List<Goods> goodses, List<CustomerSetlObjYnDto> customerSetlObjYnDtos, List<CustomerCoCdGdSeqDto> customerCoCdGdSeqDtos) {
        this.companies = companies;
        this.goodses = goodses;
        this.customerCoCdGdSeqDtos = customerCoCdGdSeqDtos;
        this.customerSetlObjYnDtos = customerSetlObjYnDtos;
    }

    public CustomerSetlObjYnDto(List<CustomerSetlObjYnDto> customerSetlObjYnDtos) {
        this.customerSetlObjYnDtos = customerSetlObjYnDtos;
    }

    public static List<Long> getSums(List<CustomerSetlObjYnDto> customerSetlObjYnDtos) {
        List<Long> results = new ArrayList<>();

        if (customerSetlObjYnDtos != null && customerSetlObjYnDtos.size() > 0) {
            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getSetlObjYCnt()).sum());
            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getSetlObjNCnt()).sum());
            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getUseStopYCnt()).sum());
            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getCnt11()).sum());
            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getUseStopNCnt()).sum());
            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getCnt12()).sum());

            long cnt11Sum = customerSetlObjYnDtos.stream().mapToLong(p -> p.getCnt21()).sum();
            long cnt12Sum = customerSetlObjYnDtos.stream().mapToLong(p -> p.getCnt22()).sum();
            results.add(cnt11Sum);
            results.add(cnt12Sum);

            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getCnt31()).sum());
            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getCnt32()).sum());
            results.add(customerSetlObjYnDtos.stream().mapToLong(p -> p.getUseStopYnTotCnt()).sum());
            results.add(cnt11Sum + cnt12Sum);
        }

        return results;
    }


    public List<CustomerSetlObjYnDto> merge() {
        List<CustomerSetlObjYnDto> results = new ArrayList<>();

        List<CustomerSetlObjYnDto> uCustSetlObjYnDtos = this.getUniqueCustomerSetlObjYnDto(customerSetlObjYnDtos);

        for (CustomerSetlObjYnDto setlObjYnDto : uCustSetlObjYnDtos) {

            setlObjYnDto.setCoCd(setlObjYnDto.getCoCd());
            setlObjYnDto.setGdCd(setlObjYnDto.getGdCd());

            Optional<Company> companyOptional = companies.stream().filter(p -> StringUtil.equals(p.getCd(), setlObjYnDto.getCoCd())).findFirst();
            if (companyOptional.isPresent()) {
                setlObjYnDto.setCoNm(companyOptional.get().getNm());
            }

            Optional<Goods> goodsOptional = goodses.stream().filter(p -> (StringUtil.equals(p.getCoCd(), setlObjYnDto.getCoCd()) && StringUtil.equals(p.getCd(), setlObjYnDto.getGdCd()))).findFirst();
            if (goodsOptional.isPresent()) {
                setlObjYnDto.setGdNm(goodsOptional.get().getNm());
            }

            Optional<CustomerCoCdGdSeqDto> cnslOptional = customerCoCdGdSeqDtos.stream().filter(
                    p -> StringUtil.equals(p.getCoCd(), setlObjYnDto.getCoCd()) && StringUtil.equals(p.getGdCd(), setlObjYnDto.getGdCd())).findFirst();
            if (cnslOptional.isPresent()) {
                setlObjYnDto.setCnt11(cnslOptional.get().getCnt11());
                setlObjYnDto.setCnt12(cnslOptional.get().getCnt12());
                setlObjYnDto.setCnt21(cnslOptional.get().getCnt21());
                setlObjYnDto.setCnt22(cnslOptional.get().getCnt22());
                setlObjYnDto.setCnt31(cnslOptional.get().getCnt31());
                setlObjYnDto.setCnt32(cnslOptional.get().getCnt32());
            }

            results.add(setlObjYnDto);
        }

        return results;
    }

    public List<CustomerSetlObjYnDto> getUniqueCustomerSetlObjYnDto(List<CustomerSetlObjYnDto> customerSetlObjYnDtos) {
        List<CustomerSetlObjYnDto> results = new ArrayList<>();

        Map<String, List<CustomerSetlObjYnDto>> groupBy = customerSetlObjYnDtos.stream()
                .collect(Collectors.groupingBy(p -> (p.getCoCd() + "|" + p.getGdCd()), TreeMap::new, Collectors.toList()));
        for (Map.Entry<String, List<CustomerSetlObjYnDto>> entry : groupBy.entrySet()) {
            String key = entry.getKey();
            List<CustomerSetlObjYnDto> values = entry.getValue();
            String[] keys = StringUtil.split(key, "|");

            long setlObjYSum_ = values.stream().mapToLong(p -> p.getSetlObjYCnt()).sum();
            long setlObjNSum_ = values.stream().mapToLong(p -> p.getSetlObjNCnt()).sum();
            long useStopYSum_ = values.stream().mapToLong(p -> p.getUseStopYCnt()).sum();
            long useStopNSum_ = values.stream().mapToLong(p -> p.getUseStopNCnt()).sum();

            CustomerSetlObjYnDto customerSetlObjYnDto = new CustomerSetlObjYnDto();
            customerSetlObjYnDto.setCoCd(keys[0]);
            customerSetlObjYnDto.setGdCd(keys[1]);
            customerSetlObjYnDto.setSetlObjYCnt(setlObjYSum_);
            customerSetlObjYnDto.setSetlObjNCnt(setlObjNSum_);
            customerSetlObjYnDto.setUseStopYCnt(useStopYSum_);
            customerSetlObjYnDto.setUseStopNCnt(useStopNSum_);
            customerSetlObjYnDto.setSetlObjYnTotCnt(setlObjYSum_ + setlObjNSum_);
            customerSetlObjYnDto.setUseStopYnTotCnt(useStopYSum_ + useStopNSum_);

            results.add(customerSetlObjYnDto);
        }

        return results;
    }

}