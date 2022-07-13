package com.aaihc.crm.biz.groundwork.domain;

import asn.util.lang.StringUtil;
import asn.util.num.NumberUtil;
import com.aaihc.crm.biz.counsel.domain.Counsel;
import com.aaihc.crm.biz.customer.domain.Customer;
import com.aaihc.crm.biz.customer.domain.CustomerApproachControl;
import com.aaihc.crm.biz.log.domain.CustomerCumulationBatch;
import com.aaihc.crm.core.config.ConfigProperty;
import com.aaihc.crm.core.domain.BaseEntityTime;
import com.aaihc.crm.core.domain.BaseRgstrNm;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.*;
import lombok.experimental.Tolerate;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>Goods (상품 Domain)</p>
 *
 * @author      : 김형수
 * date 		: 2021. 03. 11.
 * <p>
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Entity(name = "t_gw_gds")
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "rgstYmd", column = @Column(name = "gd_rgst_ymd"))
        , @AttributeOverride(name = "rgstHis", column = @Column(name = "gd_rgst_his"))
        , @AttributeOverride(name = "modYmd", column = @Column(name = "gd_mod_ymd"))
        , @AttributeOverride(name = "modHis", column = @Column(name = "gd_mod_his"))
})
@ToString(exclude = {"company", "goodsServices", "customerCumulationBatches", "customers", "counsels", "customerApproachControls"})
public class Goods extends BaseEntityTime {
    private static final long serialVersionUID = 1L;
    public static final List<Object> FMLY_SEQS = ConfigProperty.getList("groupwork.goods.familyCareGdCd.cd");
    public static final List<Object> FMLY_CARE_SEQS = ConfigProperty.getList("groupwork.goods.fcGdCd.cd");
    public static final List<Object> FMLY_CARE_PLUS_SEQS = ConfigProperty.getList("groupwork.goods.fcPlusGdCd.cd");
    public static final List<Object> HDV_SEQS = ConfigProperty.getList("groupwork.goods.hdvGdCd.cd");
    public static final List<Object> HDV_FMLY_SEQS = ConfigProperty.getList("groupwork.goods.hdvFamilyGdCd.cd");

    @Id
    @Column(name = "gd_cd")
    private String cd; // 상품코드
    @Column(name = "co_cd")
    private String coCd; // 고객사 코드
    @Column(name = "gd_nm", length = 500)
    private String nm; // 상품명
    @Column(name = "gd_sale_mthd", length = 500)
    private String saleMthd; // 판매방식
    @Column(name = "gd_excls_no", length = 50)
    private String exclsNo; // 전용번호
    @Column(name = "gd_objr", length = 10)
    private String objr; // 대상자
    @Column(name = "gd_objr_dtl", length = 20)
    private String objrDtl; // 대상자 상세
    @Column(name = "gd_aply_wrt_yn", length = 1)
    private String aplyWrtYn; // 신청서작성여부
    @Column(name = "gd_dtl_cont", length = 4000)
    private String dtlCont; // 상세내용
    @Column(name = "gd_pculr", length = 500)
    private String pculr; // 특이사항
    @Column(name = "gd_ctrt_ymd", length = 8)
    private String ctrtYmd; // 계약일자
    @Column(name = "gd_ctrt_strt_ymd", length = 8)
    private String ctrtStrtYmd; // 계약시작일
    @Column(name = "gd_ctrt_end_ymd", length = 8)
    private String ctrtEndYmd; // 계약종료일
    @Column(name = "gd_hlcr_strt_ymd", length = 8)
    private String hlcrStrtYmd; // 헬스케어제공 시작일
    @Column(name = "gd_hlcr_end_ymd", length = 8)
    private String hlcrEndYmd; // 헬스케어제공 종료일
    @Column(name = "gd_svc_ofr_prd", length = 500)
    private String svcOfrPrd; // 서비스 제공 기간
    @Column(name = "gd_svc_ofr_schdl", length = 500)
    private String svcOfrSchdl; // 서비스 제공 스케줄
    @Column(name = "gd_mbr_upld_schdl", length = 500)
    private String mbrUpldSchdl; // 회원 업로드 스케줄
    @Column(name = "gd_mbr_info_itm", length = 20)
    private String mbrInfoItm; // 회원 정보 항목
    @Column(name = "gd_mbr_canc_schdl", length = 500)
    private String mbrCancSchdl; // 회원 해지 스케줄
    @Column(name = "gd_mbr_desc", length = 4000)
    private String mbrDesc; // 회원 설명
    @Column(name = "gd_ctrt_amt", length = 200)
    private String ctrtAmt; // 계약금액
    @Column(name = "gd_ctrt_use_yn", length = 1)
    private String ctrtUseYn; // 계약사용여부
    @Column(name = "gd_use_yn", length = 1)
    private String useYn; // 사용여부
    @Column(name = "gd_gds_svc_cnt")
    private int gdsSvcCnt; // 상품서비스수
    @Column(name = "gd_cust_rgst_cnt")
    private int custRgstCnt; // 고객등록수
    @Column(name = "gd_cust_extnc_cnt")
    private int custExtncCnt; // 고객소멸수
    @Column(name = "gd_cust_curr_cnt")
    private int custCurrCnt; // 고객현재수
    @Column(name = "gd_cnsl_cnt")
    private int cnslCnt; // 상담수
    @Column(name = "asis_goods_code", length = 4)
    private String goodsCode; // ASIS 상품 코드

    @Transient
    private String nmWhCop; // 제휴여부 포함 명

    @CreatedBy
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "gd_rgstr_id"))
            , @AttributeOverride(name = "nm", column = @Column(name = "gd_rgstr_nm"))
    })
    private BaseRgstrNm rgstr;

    @CreatedBy
    // @LastModifiedBy
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "gd_modr_id"))
            , @AttributeOverride(name = "nm", column = @Column(name = "gd_modr_nm"))
    })
    private BaseRgstrNm modr;

    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "co_cd", insertable = false, updatable = false)
    private Company company;    // 고객사

    @JsonManagedReference
    @OneToMany(mappedBy = "goods")
    private List<GoodsService> goodsServices = new ArrayList<>();   // 상품 서비스

    @JsonManagedReference
    @OneToMany(mappedBy = "goods")
    private List<CustomerCumulationBatch> customerCumulationBatches = new ArrayList<>(); // 고객 누적 배치

    @JsonManagedReference
    @OneToMany(mappedBy = "goods")
    private List<Customer> customers = new ArrayList<>(); // 고객

    @JsonManagedReference
    @OneToMany(mappedBy = "goods")
    private List<Counsel> counsels = new ArrayList<>(); // 상담

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    private List<CustomerApproachControl> customerApproachControls = new ArrayList<>(); // 고객접근제어

    @Transient
    private List<String> mbrInfoItms;

    /**
     * <p>상품 목록을 맵</p>
     *
     * @param goodses 상품 목록
     * @return 맵
     */
    public static ListOrderedMap getMap(List<Goods> goodses) {
        ListOrderedMap result = new ListOrderedMap();

        for (Goods goods : goodses) {
            result.put(goods.getCd(), goods.getNm());
        }

        return result;
    }

    /**
     * <p>JSON을 생성합니다.</p>
     *
     * @param goodses 상품목록
     * @return 도메인
     */
    public static String ofJson(List<Goods> goodses) {
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();

        if (goodses != null) {
            for (Goods goods : goodses) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("cd", goods.getCd());

                StringBuilder sb = new StringBuilder();
                if (StringUtil.equals(goods.getUseYn(), "Y")) {
                    sb.append("[서비스 제공]");
                } else {
                    sb.append("[서비스 종료]");
                }

                if (StringUtil.equals(goods.getCtrtUseYn(), "Y")) {
                    sb.append("[계약 유지]");
                } else {
                    sb.append("[계약 종료]");
                }

                sb.append(goods.getNm());
                jsonObject.addProperty("nm", sb.toString());

                jsonArray.add(jsonObject);
            }
        }

        return gson.toJson(jsonArray);
    }

    /**
     * <p>신청서 작성여부 맵</p>
     *
     * @return ListOrderedMap
     */
    public static ListOrderedMap getAplyWrtYnMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("Y", "작성");
        result.put("N", "미작성");

        return result;
    }

    /**
     * <p>서비스 제공기간 맵</p>
     *
     * @return ListOrderedMap
     */
    public static ListOrderedMap getSvcOfrPrdMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("12", "1년");
        result.put("24", "2년");
        result.put("36", "3년");
        result.put("48", "4년");
        result.put("60", "5년");
        result.put("72", "6년");
        result.put("84", "7년");
        result.put("96", "8년");
        result.put("108", "9년");
        result.put("120", "10년");
        result.put("180", "15년");
        result.put("240", "20년");
        result.put("1200", "100년");

        return result;
    }

    /**
     * <p>회원정보항목 맵</p>
     *
     * @return ListOrderedMap
     */
    public static ListOrderedMap getMbrInfoItmMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("0", "정보없음");
        result.put("1", "가족관계");
        result.put("2", "성명");
        result.put("3", "생년월일");
        result.put("4", "성별");
        result.put("5", "휴대폰");
        result.put("6", "E-mail");
        result.put("7", "주소");
        result.put("8", "외부식별코드");
        result.put("9", "증권번호");

        return result;
    }

    /**
     * <p>대상자 맵</p>
     *
     * @return ListOrderedMap
     */
    public static ListOrderedMap getObjrMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("1", "본인");
        result.put("2", "본인+가족");

        return result;
    }

    /**
     * <p>회원 서비스 기간을 구합니다.</p>
     *
     * @return 목록
     */
    public int getSvcOfrPrdYear() {
        if (StringUtil.isNotBlank(this.getSvcOfrPrd())) {
            return NumberUtil.toInt(this.getSvcOfrPrd()) / 12;
        }

        return 0;
    }

    /**
     * <p>회원 정보 항목 목록을 구합니다.</p>
     *
     * @return 목록
     */
    public String[] getMbrInfoItmArray() {
        String[] results = null;
        if(StringUtil.isNotBlank(this.getMbrInfoItm())) {
            results = StringUtil.split(this.getMbrInfoItm(), ",");
        }

        return results;
    }

    /**
     * <p>회원 정보 항목 문자열을 설정합니다.</p>
     */
    @Tolerate
    public void setMbrInfoItm() {
        StringBuffer sb = new StringBuffer();
        List<String> mbrInfoItms = this.getMbrInfoItms();

        if (mbrInfoItms != null) {
            for (int i = 0; i < mbrInfoItms.size(); i++){
                if (StringUtil.isNotBlank(mbrInfoItms.get(i))) {
                    sb.append(mbrInfoItms.get(i));

                    if (i < mbrInfoItms.size() - 1) {
                        sb.append(",");
                    }
                }
            }

            this.setMbrInfoItm(sb.toString());
        }
    }

    public String getNmWhCop() {
        String val = "";

        if (StringUtil.equals("Y", this.getUseYn())) {
            val = "[서비스] " + this.getNm();
        } else if (StringUtil.equals("N", this.getUseYn())) {
            val = "[종료] " + this.getNm();
        } else {
            val = "[확인] " + this.getNm();
        }

        return val;
    }

}