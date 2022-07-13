package com.aaihc.crm.biz.customer.domain;

import asn.util.date.DateFormatUtil;
import asn.util.date.DateUtil;
import asn.util.lang.ArrayUtil;
import asn.util.lang.StringUtil;
import asn.util.num.NumberUtil;
import com.aaihc.crm.biz.counsel.domain.Counsel;
import com.aaihc.crm.biz.groundwork.domain.Company;
import com.aaihc.crm.biz.groundwork.domain.Goods;
import com.aaihc.crm.core.converter.EncodeS;
import com.aaihc.crm.core.converter.EncodeT;
import com.aaihc.crm.core.domain.BaseEntityTime;
import com.aaihc.crm.core.domain.BaseRgstrNm;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static javax.persistence.FetchType.LAZY;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>Customer (고객 Domain)</p>
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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Entity(name = "t_cu_cust")
@SequenceGenerator(name = "t_seq_cu_cust_gen", sequenceName = "t_seq_cu_cust", allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "rgstYmd", column = @Column(name = "cu_rgst_ymd"))
        , @AttributeOverride(name = "rgstHis", column = @Column(name = "cu_rgst_his"))
        , @AttributeOverride(name = "modYmd", column = @Column(name = "cu_mod_ymd"))
        , @AttributeOverride(name = "modHis", column = @Column(name = "cu_mod_his"))
})
@ToString(exclude = {"company", "goods", "customerTfas", "counsels", "customerMappings", "customerRevivals"})
public class Customer extends BaseEntityTime {
    private static final long serialVersionUID = 1L;
    public static final String[] MAPPING_COCDS = new String[]{"A00", "A01", "X01", "X14", "X21", "N03"};

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "t_seq_cu_cust_gen")
    @Column(name = "cu_seq")
    private long seq; // 고객 일련번호
    @Column(name = "co_cd", length = 3)
    private String coCd; // 고객사코드
    @Column(name = "gd_cd", length = 3)
    private String gdCd; // 상품코드
    @Column(name = "cu_nm", length = 600)
    private String nm; // 이름
    @Column(name = "cu_grp", length = 500)
    private String grp; // 그룹
    @Column(name = "cu_extnl_certi_cd", length = 10)
    private String extnlCertiCd; // 외부인증코드
    @Column(name = "cu_brtdy", length = 10)
    private String brtdy; // 생년월일
    @Column(name = "cu_sex", length = 1)
    private String sex; // 성별
    @Column(name = "cu_cph", length = 450)
    @Convert(converter = EncodeT.class)
    private String cph; // 핸드폰
    @Column(name = "cu_email", length = 750)
    @Convert(converter = EncodeS.class)
    private String email; // 이메일
    @Column(name = "cu_co_ofr_svc_cd", length = 50)
    private String coOfrSvcCd; // 고객사제공서비스코드
    @Column(name = "cu_co_ofr_mbr_rgst_ymd", length = 8)
    private String coOfrMbrRgstYmd; // 고객사제공회원등록일
    @Column(name = "cu_co_ofr_svc_nm", length = 500)
    private String coOfrSvcNm; // 고객사제공서비스명
    @Column(name = "cu_setl_obj_yn", length = 1)
    private String setlObjYn; // 정산대상여부
    @Column(name = "cu_zipcd", length = 50)
    private String zipcd; // 우편번호
    @Column(name = "cu_base_addr", length = 500)
    private String baseAddr; // 주소
    @Column(name = "cu_dtl_addr", length = 500)
    private String dtlAddr; // 상세주소
    @Column(name = "cu_use_stop_yn", length = 1)
    private String useStopYn; // 사용중지여부
    @Column(name = "cu_sms_rcv_rejct_yn", length = 1)
    private String smsRcvRejctYn; // SMS수신거부여부
    @Column(name = "cu_email_rcv_rejct_yn", length = 1)
    private String emailRcvRejctYn; // 이메일수신거부여부
    @Column(name = "cu_tel_rcv_rejct_yn", length = 1)
    private String telRcvRejctYn; // 전화수신거부여부
    @Column(name = "cu_svc_use_guid_ltr_yn", length = 1)
    private String svcUseGuidLtrYn; // 서비스사용안내문자여부
    @Column(name = "cu_svc_tel", length = 600)
    @Convert(converter = EncodeT.class)
    private String svcTel; // 서비스 전화
    @Column(name = "cu_pculr", length = 2000)
    private String pculr; // 특이사항
    @Column(name = "cu_svc_srl", length = 100)
    private String svcSrl; // 서비스 시리얼
    @Column(name = "cu_svc_auth", length = 100)
    private String svcAuth; // 서비스 권한
    @Column(name = "cu_acnt", length = 100)
    private String acnt; // 고객 계정명
    @Column(name = "cu_svc_end_ymd", length = 10)
    private String svcEndYmd; // 서비스 종료일
    @Column(name = "cu_tel", length = 600)
    @Convert(converter = EncodeT.class)
    private String tel; // 고객전화
    @Column(name = "cu_agree_yn1", length = 1)
    private String agreeYn1; // 동의여부-개인정보수집
    @Column(name = "cu_agree_yn2", length = 1)
    private String agreeYn2; // 동의여부-약관
    @Column(name = "cu_agree_yn3", length = 1)
    private String agreeYn3; // 동의여부-마케팅
    @Column(name = "cu_agree_ymdt1", length = 14)
    private String agreeYmdt1; // 개인정보동의일시
    @Column(name = "cu_agree_ymdt2", length = 14)
    private String agreeYmdt2; // 약관동의일시
    @Column(name = "cu_agree_ymdt3", length = 14)
    private String agreeYmdt3; // 마케팅동의일시
    @Column(name = "cu_rvvl_ymd", length = 8)
    private String rvvlYmd; // 부활일자
    @Column(name = "cu_dsis1_objr_yn", length = 1)
    private String dsis1ObjrYn; // 삼성생명 암뇌심 대상자여부
    @Column(name = "cu_dsis1_objr_ymd", length = 8)
    private String dsis1ObjrYmd; // 삼성생명 암뇌심 대장자여부 일
    @Column(name = "cu_svc_tp_cd", length = 2)
    private String svcTpCd; // 서비스 유형코드
    @Column(name = "cu_svc_prd", length = 100)
    private String svcPrd; // 서비스기간
    @Column(name = "cu_htnd1_itm1_ymdt", length = 1)
    private String htnd1Itm1Ymdt; // 질병력 암여부
    @Column(name = "cu_htnd1_itm2_ymdt", length = 1)
    private String htnd1Itm2Ymdt; // 질병력 심혈관여부
    @Column(name = "cu_htnd1_itm3_ymdt", length = 1)
    private String htnd1Itm3Ymdt; //  질병력 뇌혈관여부
    @Column(name = "cu_htnd1_itm4_ymdt", length = 1)
    private String htnd1Itm4Ymdt; // 질병력 고혈압여부
    @Column(name = "cu_htnd1_itm5_ymdt", length = 1)
    private String htnd1Itm5Ymdt; //  질병력 당뇨여부
    @Column(name = "cu_htnd1_itm6_ymdt", length = 1)
    private String htnd1Itm6Ymdt; // 질병력 기타여부
    @Column(name = "cu_htnd1_itm6", length = 400)
    private String htnd1Itm6; // 질병력 기타
    @Column(name = "cu_htnd2_itm1_ymdt", length = 1)
    private String htnd2Itm1Ymdt; // 가족력 암여부
    @Column(name = "cu_htnd2_itm2_ymdt", length = 1)
    private String htnd2Itm2Ymdt; // 가족력 심혈관여부
    @Column(name = "cu_htnd2_itm3_ymdt", length = 1)
    private String htnd2Itm3Ymdt; // 가족력 뇌혈관여부
    @Column(name = "cu_htnd2_itm4_ymdt", length = 1)
    private String htnd2Itm4Ymdt; // 가족력 고혈압여부
    @Column(name = "cu_htnd2_itm5_ymdt", length = 1)
    private String htnd2Itm5Ymdt; // 가족력 당뇨여부
    @Column(name = "cu_htnd2_itm6_ymdt", length = 1)
    private String htnd2Itm6Ymdt; // 가족력 기타여부
    @Column(name = "cu_htnd2_itm6", length = 400)
    private String htnd2Itm6; // 가족력 기타
    @Column(name = "cu_htnd3_itm1_ymdt", length = 1)
    private String htnd3Itm1Ymdt; // 관심질병 암여부
    @Column(name = "cu_htnd3_itm2_ymdt", length = 1)
    private String htnd3Itm2Ymdt; // 관심질병 심혈관여부
    @Column(name = "cu_htnd3_itm3_ymdt", length = 1)
    private String htnd3Itm3Ymdt; // 관심질병 뇌혈관여부
    @Column(name = "cu_htnd3_itm4_ymdt", length = 1)
    private String htnd3Itm4Ymdt; // 관심질병 고혈압여부
    @Column(name = "cu_htnd3_itm5_ymdt", length = 1)
    private String htnd3Itm5Ymdt; // 관심질병 당뇨여부
    @Column(name = "cu_htnd3_itm6_ymdt", length = 1)
    private String htnd3Itm6Ymdt; // 관심질병 기타여부
    @Column(name = "cu_htnd3_itm6", length = 400)
    private String htnd3Itm6; // 가족력 기타
    @Column(name = "cu_htnd4_memo", length = 400)
    private String htnd4Memo; // 질병메모
    @Column(name = "cu_mg_tp", length = 1)
    private String mgTp; // 관리유형
    @Column(name = "cu_cvcpt_tdc_tp", length = 1)
    private String cvcptTdcTp; // 민원고객성향구분
    @Column(name = "cu_excls_mgr_tp", length = 1)
    private String exclsMgrTp; // 전용관리자구분
    @Column(name = "cu_cnsl_cnt")
    private int cnslCnt; // 상담수
    @Column(name = "cu_rvvl_cnt")
    private int rvvlCnt; // 부활수
    @Column(name = "cu_lst_prnt_call_cnsl_ymdt1")
    private String lstPrntCallCnslYmdt1; // 최종질환효도콜상담일
    @Column(name = "cu_lst_prnt_call_cnsl_ymdt2")
    private String lstPrntCallCnslYmdt2; // 최종효도콜상담일
    @Column(name = "cu_lst_prnt_call_rslt")
    private String lstPrntCallRslt; // 최종효도콜결과
    @Column(name = "cu_lst_care_call_cnsl_ymdt")
    private String lstCareCallCnslYmdt; // 최종안부콜상담일
    @Column(name = "cu_lst_care_call_rslt")
    private String lstCareCallRslt; // 최종안부콜결과
    @Column(name = "cu_extnc_yn", length = 1)
    private String extncYn; // 소멸여부
    @Column(name = "cu_extnc_ymd", length = 8)
    private String extncYmd; // 소멸일
    @Column(name = "cu_extnc_his", length = 6)
    private String extncHis; // 소멸시간
    @Column(name = "asis_customer_code", length = 32)
    private String customerCode; //

    @CreatedBy
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "cu_rgstr_id"))
            , @AttributeOverride(name = "nm", column = @Column(name = "cu_rgstr_nm"))
    })
    private BaseRgstrNm rgstr;

    @CreatedBy
    // @LastModifiedBy
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "cu_modr_id"))
            , @AttributeOverride(name = "nm", column = @Column(name = "cu_modr_nm"))
    })
    private BaseRgstrNm modr;

    @NotFound(action= NotFoundAction.IGNORE)
    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "co_cd", insertable = false, updatable = false)
    private Company company;    // 고객사

    @NotFound(action= NotFoundAction.IGNORE)
    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "gd_cd", insertable = false, updatable = false)
    private Goods goods;    // 상품

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<CustomerTfa> customerTfas = new ArrayList<>();   // 고객 TFA

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<Counsel> counsels = new ArrayList<>();   // 상담

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<CustomerMapping> customerMappings = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<CustomerRevival> customerRevivals = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<CustomerCounselService> customerCounselServices = new ArrayList<>();

    @Transient
    private String htnd1Itm1Yn;
    @Transient
    private String htnd1Itm2Yn;
    @Transient
    private String htnd1Itm3Yn;
    @Transient
    private String htnd1Itm4Yn;
    @Transient
    private String htnd1Itm5Yn;
    @Transient
    private String htnd1Itm6Yn;
    @Transient
    private String htnd2Itm1Yn;
    @Transient
    private String htnd2Itm2Yn;
    @Transient
    private String htnd2Itm3Yn;
    @Transient
    private String htnd2Itm4Yn;
    @Transient
    private String htnd2Itm5Yn;
    @Transient
    private String htnd2Itm6Yn;
    @Transient
    private String htnd3Itm1Yn;
    @Transient
    private String htnd3Itm2Yn;
    @Transient
    private String htnd3Itm3Yn;
    @Transient
    private String htnd3Itm4Yn;
    @Transient
    private String htnd3Itm5Yn;
    @Transient
    private String htnd3Itm6Yn;

    @PrePersist
    public void prePersist2() {
        useStopYn = "Y";
        setlObjYn = "Y";
        extncYn = "N";

        mgTp = "0";
    }

    /**
     * <p>성별 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getSexMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("M", "남");
        result.put("F", "여");

        return result;
    }
    
    /**
     * <p>소멸유무 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getUseStopYnMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("Y", "유효");
        result.put("N", "소멸");

        return result;
    }

    /**
     * <p>소멸유무 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getSetlObjYnMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("Y", "유료");
        result.put("N", "무료");
        return result;
    }

    /**
     * <p>소멸유무 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getSvcExpMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("Y", "만료");
        result.put("N", "진행");
        return result;
    }

    /**
     * <p>등록구분 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getBatYnMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("Y", "배치");
        result.put("N", "개별");
        return result;
    }

    /**
     * <p>서비스안내문자여부 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getSvcUseGuidLtrYnMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("Y", "전송");
        result.put("N", "미전송");
        return result;
    }

    /**
     * <p>관리유형 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getMgTpMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("1", "해당없음");
        result.put("2", "충성고객");
        result.put("3", "이용고객");
        result.put("4", "민원고객");
        return result;
    }

    /**
     * <p>고객성향 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getCvcptTdcTpMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("3", "3단계");
        result.put("2", "2단계");
        result.put("1", "1단계");
        return result;
    }

    /**
     * <p>전용관리자구분 맵</p>
     *
     * @return 맵
     */
    public static ListOrderedMap getExclsMgrTpMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("1", "위플");
        return result;
    }

    /**
     * <p>액셀 컬럼 이름 객체를 생성합니다.</p>
     *
     * @return 도메인
     */
    public static List<String> getExcelColNmsHappyCall() {
        List<String> results = new ArrayList<>();
        results.add("No");
        results.add("고객사명");
        results.add("상품명");
        results.add("서비스등록일");
        results.add("회원명");
        results.add("생년월일");
        results.add("성별");
        results.add("전화번호");

        return results;
    }

    /**
     * <p>액셀 객체를 생성합니다.</p>
     *
     * @param customers 목록
     * @return 도메인
     */
    public static List<List<String>> ofExcelHappyCall(List<Customer> customers) {
        List<List<String>> results = new ArrayList<>();
        int index = 1;

        for (Customer customer : customers) {
            List<String> cols = new ArrayList<>();
            cols.add(StringUtil.toString(index));
            cols.add(StringUtil.toString(customer.getCompany().getNm()));
            cols.add(StringUtil.toString(customer.getGoods().getNm()));
            cols.add(StringUtil.toString(customer.getCoOfrMbrRgstYmd()));
            cols.add(StringUtil.toString(customer.getNm()));
            cols.add(StringUtil.toString(customer.getBrtdy()));
            cols.add(StringUtil.toString(Customer.getSexMap().get(customer.getSex())));
            cols.add(StringUtil.toString(customer.getCph()));

            results.add(cols);
            index++;
        }

        return results;
    }

    public CustomerMapping getCustomerMapping() {
        CustomerMapping customerMapping = null;

        Optional<CustomerMapping> customerMappingOptional = this.getCustomerMappings().stream().filter(p -> StringUtil.isNotBlank(p.getId())).findFirst();
        if (customerMappingOptional.isPresent()) {
            customerMapping = customerMappingOptional.get();
        }

        return customerMapping;
    }

    /**
     * <p>서비스 종료일을 구합니다.</p>
     *
     * @return 목록
     */
    public String getSvcEndDate() {
        String result = "";
        Goods goods = this.getGoods();

        if (goods != null) {
            if (StringUtil.isNotBlank(this.getSvcEndYmd())) {
                return this.getSvcEndYmd();
            }

            if (StringUtil.isNotBlank(this.getCoOfrMbrRgstYmd())) {
                String svcOfrPrd = goods.getSvcOfrPrd();
                result = DateUtil.addMonths(this.getCoOfrMbrRgstYmd(), NumberUtil.toInt(svcOfrPrd));
            }
        }

        return result;
    }

    /**
     * <p>고객매핑여부를 구합니다.</p>
     *
     * @return 목록
     */
    public boolean isMapping() {
        return ArrayUtil.contains(MAPPING_COCDS, this.getCoCd());
    }

    /**
     * <p>생년월일로 건강검진기간 여부를 구합니다.</p>
     *
     * @return 목록
     */
    public boolean isCkupTerm() {
        int brtYear = NumberUtil.toInt(StringUtil.left(this.getBrtdy(), 2));
        int currYear = NumberUtil.toInt(DateFormatUtil.format(new Date(), "yy"));
        if (brtYear % 2 == currYear % 2) {
            return true;
        }

        return false;
    }

    /**
     * <p>고객 상담 서비스 총회수를 구합니다.</p>
     *
     * @param gsSeq 상품서비스 일련번호
     * @return 값
     */
    public double getRstctDdtnCnt(long gsSeq) {
        double val = 0d;

        Optional<CustomerCounselService> optional = this.getCustomerCounselServices().stream().filter(p -> p.getGsSeq() == gsSeq).findFirst();
        if (optional.isPresent()) {
            val = NumberUtil.toDouble(optional.get().getRstctDdtnCnt());
        }
        return val;
    }

}