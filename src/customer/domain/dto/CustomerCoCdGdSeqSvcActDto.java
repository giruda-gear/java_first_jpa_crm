package com.aaihc.crm.biz.customer.domain.dto;

import asn.util.lang.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCoCdGdSeqSvcActDto (고객 Dto)</p>
 *
 * @author 	    : 양용수
 * date 		: 2021. 03. 18.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerCoCdGdSeqSvcActDto  {
    private static final long serialVersionUID = 1L;

    private String coCd;
    private String coNm;
    private Character coUseYn;
    private String gdCd;
    private String gdNm;
    private Character gdUseYn;
    private String gdCtrtYn;
    private Character setlObjYn;
    private Character useStopYn;
    private String actDesc;
    private int cnt;

    public CustomerCoCdGdSeqSvcActDto(String coCd, String gdCd, Character setlObjYn, Character useStopYn, String actDesc, int cnt, String coNm, Character coUseYn, String gdNm, Character gdUseYn, String gdCtrtYn) {
        this.coCd = coCd;
        this.gdCd = gdCd;
        this.setlObjYn = setlObjYn;
        this.useStopYn = useStopYn;
        this.actDesc = actDesc;
        this.cnt = cnt;
        this.coNm = coNm;
        this.coUseYn = coUseYn;
        this.gdNm = gdNm;
        this.gdUseYn = gdUseYn;
        this.gdCtrtYn = gdCtrtYn;
    }

    public String getGdNmPrefix() {
        StringBuffer sbPrefix = new StringBuffer();
        String gdCtrtUseYn = StringUtil.toString(this.getGdCtrtYn());
        String gdUseYn = StringUtil.toString(this.getGdUseYn());

        if (StringUtil.equals(gdCtrtUseYn, "Y")) {
            sbPrefix.append("[계약 유지]");
        } else {
            sbPrefix.append("[계약 중지]");
        }

        if (StringUtil.equals(gdUseYn, "Y")) {
            sbPrefix.append("[서비스 진행]");
        } else {
            sbPrefix.append("[서비스 종료]");
        }

        return sbPrefix.toString();
    }

    public String getStat() {
        String result;

        if (this.getUseStopYn() == 'Y') {
            result = "유지 고객";
        } else {
            result = "소멸 고객";
        }

        return result;
    }

    /**
     * <p>등록구분 맵</p>
     *
     * @return 값
     */
    public String getActDescDtl() {
        String actDesc = this.getActDesc();

        if (StringUtil.equals(actDesc, "system")) {
            return "배치";
        } else if  (StringUtil.equals(actDesc, "aaihc")) {
            return "AAIHC";
        } else {
            return "개별";
        }
    }

//    public long getSumTotCnt() {
//
//    }

}