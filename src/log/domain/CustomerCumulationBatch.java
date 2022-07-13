package com.aaihc.crm.biz.log.domain;

import asn.util.date.DateFormatUtil;
import asn.util.date.DateUtil;
import asn.util.lang.StringUtil;
import asn.util.num.NumberUtil;
import com.aaihc.crm.biz.groundwork.domain.Company;
import com.aaihc.crm.biz.groundwork.domain.Goods;
import com.aaihc.crm.biz.security.domain.User;
import com.aaihc.crm.core.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCumulationBatch (고객 누적 배치 Domain)</p>
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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Entity(name = "t_lo_cust_cmltn_bat")
@SequenceGenerator(name = "t_seq_lo_cust_cmltn_bat_gen", sequenceName = "t_seq_lo_cust_cmltn_bat", allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"company", "goods"})
public class CustomerCumulationBatch extends BaseDomain {
    private static final long serialVersionUID = 1L;

    private static final int MIN_YEAR = 2015; // 연도 검색 최솟값

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_lo_cust_cmltn_bat_gen")
    @Column(name = "ccb_seq")
    private long seq; // 고객 누적 배치 일련번호
    @Column(name = "ccb_rcv_tp", length = 100)
    private String rcvTp; // 수신방법
    @Column(name = "ccb_rcv_ip", length = 20)
    private String rcvIp; // 수신IP
    @Column(name = "ccb_rcv_port", length = 10)
    private String rcvPort; // 수신PORT
    @Column(name = "ccb_rcv_rmrk", length = 4000)
    private String rcvRmrk; // 수신비고
    @Column(name = "ccb_rcv_desc", length = 4000)
    private String rcvDesc; // 수신설명
    @Column(name = "ccb_file_path", length = 4000)
    private String filePath; // 파일경로
    @Column(name = "ccb_file_cyc", length = 100)
    private String fileCyc; // 파일주기
    @Column(name = "ccb_file_grp", length = 100)
    private String fileGrp; // 파일그룹
    @Column(name = "ccb_file_tp", length = 100)
    private String fileTp; // 파일구분
    @Column(name = "ccb_file_nm", length = 4000)
    private String fileNm; // 파일명
    @Column(name = "ccb_file_dlmt", length = 500)
    private String fileDlmt; // 파일구분자
    @Column(name = "ccb_file_sht", length = 500)
    private String fileSht; // 파일시트
    @Column(name = "ccb_file_strt_ln")
    private int fileStrtLn; // 파일시작라인
    @Column(name = "ccb_file_fld_use", length = 4000)
    private String fileFldUse; // 파일필드사용
    @Column(name = "ccb_file_fld_dup", length = 4000)
    private String fileFldDup; // 파일필드중복
    @Column(name = "ccb_file_fld_chc", length = 4000)
    private String fileFldChc; // 파일필드선택
    @Column(name = "ccb_file_curr_yn", length = 1)
    private String fileCurrYn; // 파일현재여부
    @Column(name = "ccb_file_rmrk", length = 4000)
    private String fileRmrk; // 파일비고
    @Column(name = "ccb_file_desc", length = 4000)
    private String fileDesc; // 파일설명
    @Column(name = "ccb_cust_tret_ymd", length = 8)
    private String custTretYmd; // 고객처리일
    @Column(name = "ccb_tot_cnt")
    private int totCnt; // 합계
    @Column(name = "ccb_cust_vld_cnt")
    private int custVldCnt; // 고객유효수
    @Column(name = "ccb_cust_new_cnt")
    private int custNewCnt; // 고객신규수
    @Column(name = "ccb_cust_maint_cnt")
    private int custMaintCnt; // 고객유지수
    @Column(name = "ccb_cust_dup_cnt")
    private int custDupCnt; // 고객중복수
    @Column(name = "ccb_cust_err_cnt")
    private int custErrCnt; // 고객에러수
    @Column(name = "ccb_cust_del_cnt")
    private int custDelCnt; // 고객삭제수
    @Column(name = "ccb_cust_ps_ymd", length = 8)
    private String custPsYmd; // 고객처리날짜
    @Column(name = "ccb_cust_rmrk", length = 4000)
    private String custRmrk; // 고객비고
    @Column(name = "ccb_cust_dtl", length = 4000)
    private String custDtl; // 고객상세
    @Column(name = "ccb_sms_tot")
    private int smsTot; // SMS전송대기
    @Column(name = "ccb_sms_sed")
    private int smsSed; // SMS전송
    @Column(name = "ccb_sms_dny")
    private int smsDny; // SMS전송불가
    @Column(name = "ccb_sms_rmrk", length = 4000)
    private String smsRmrk; // SMS비고
    @Column(name = "ccb_sms_desc", length = 4000)
    private String smsDesc; // SMS설명
    @Column(name = "ccb_setl_amt")
    private int setlAmt; // 정산금액
    @Column(name = "ccb_setl_rmrk", length = 4000)
    private String setlRmrk; // 정산비고
    @Column(name = "ccb_setl_desc", length = 4000)
    private String setlDesc; // 정산설명
    @Column(name = "ccb_tel_not_cnt")
    private int telNotCnt; // 전화번호없음수
    @Column(name = "ccb_rgstr_id", length = 50)
    private String rgstrId; // 등록자아이디
    @Column(name = "ccb_rgst_ymdt", length = 14)
    private String rgstYmdt; // 등록일시

    @NotFound(action= NotFoundAction.IGNORE)
    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "co_cd")
    private Company company;    // 서비스

    @NotFound(action= NotFoundAction.IGNORE)
    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "gd_cd")
    private Goods goods;    // 상품

    @Transient

    @PrePersist
    public void prePersist() {
        this.rgstYmdt = super.getCurrYmd() + super.getCurrHis();
    }

    /**
     * <p>소멸유무 맵</p>
     *
     * @return 값
     */
    public static ListOrderedMap getFileCycMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("daily", "D 매일");
        result.put("weekly", "W 매주");
        result.put("monthly", "M 매월");

        return result;
    }

    /**
     * <p>액셀 컬럼 이름 객체를 생성합니다.</p>
     *
     * @return 도메인
     */
    public static List<String> getExcelColNms() {
        List<String> results = new ArrayList<>();
        results.add("고객사명");
        results.add("상품명");
        results.add("수신방법");
        results.add("수신설명");
        results.add("파일주기");
        results.add("파일그룹");
        results.add("파일구분");
        results.add("파일명");
        results.add("파일설명");
        results.add("수신파일날짜");
        results.add("회원처리항목(이름)");
        results.add("회원처리항목(생년월일)");
        results.add("회원처리항목(성별)");
        results.add("회원처리항목(전화번호)");
        results.add("회원처리항목(주소)");
        results.add("회원처리항목(가족관계)");
        results.add("회원처리항목(E-mail)");
        results.add("회원처리항목(고객사제공 일련번호)");
        results.add("회원처리항목(고객사제공 증권번호)");
        results.add("회원처리항목(고객사제공 인증번호)");
        results.add("원본(합계)");
        results.add("유효회원");
        results.add("신규회원");
        results.add("유지회원");
        results.add("중복회원");
        results.add("에러회원");
        results.add("삭제회원");
        results.add("회원처리날짜");
        results.add("정산비고");
        results.add("정산설명");
        results.add("SMS전송대기");
        results.add("SMS전송");
        results.add("SMS전송불가");
        results.add("SMS비고");
        results.add("SMS설명");
        results.add("전화번호없음");
        results.add("등록자");

        return results;
    }

    /**
     * <p>액셀 객체를 생성합니다.</p>
     *
     * @param customerCumulationBatches 목록
     * @param users 사용자목록
     * @return 도메인
     */
    public static List<List<String>> ofExcel(List<CustomerCumulationBatch> customerCumulationBatches, List<User> users) {
        List<List<String>> results = new ArrayList<>();

        for (CustomerCumulationBatch customerCumulationBatch : customerCumulationBatches) {
            List<String> cols = new ArrayList<>();
            String coNm = "";
            String gdNm = "";
            if (customerCumulationBatch.getCompany() != null) {
                coNm = customerCumulationBatch.getCompany().getNm();
            }
            if (customerCumulationBatch.getGoods() != null) {
                gdNm = customerCumulationBatch.getGoods().getNm();
            }
            cols.add(StringUtil.toString(coNm));
            cols.add(StringUtil.toString(gdNm));
            cols.add(StringUtil.toString(customerCumulationBatch.getRcvRmrk()));
            cols.add(StringUtil.toString(customerCumulationBatch.getRcvDesc()));
            cols.add(StringUtil.toString(customerCumulationBatch.getFileCyc()));
            cols.add(StringUtil.toString(customerCumulationBatch.getFileGrp()));
            cols.add(StringUtil.toString(customerCumulationBatch.getFileTp()));
            cols.add(StringUtil.toString(customerCumulationBatch.getFileNm()));
            cols.add(StringUtil.toString(customerCumulationBatch.getFileDesc()));
            cols.add(DateFormatUtil.format(StringUtil.toString(customerCumulationBatch.getCustTretYmd()), "yyyy-MM-dd"));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("01")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("02")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("03")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("04")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("05")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("06")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("07")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("08")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("09")));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustProcChk("10")));
            cols.add(StringUtil.toString(customerCumulationBatch.getTotCnt()));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustVldCnt()));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustNewCnt()));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustMaintCnt()));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustDupCnt()));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustDelCnt()));
            cols.add(DateFormatUtil.format(StringUtil.toString(customerCumulationBatch.getCustPsYmd()), "yyyy-MM-dd"));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustRmrk()));
            cols.add(StringUtil.toString(customerCumulationBatch.getCustDtl()));
            cols.add(StringUtil.toString(customerCumulationBatch.getSmsTot()));
            cols.add(StringUtil.toString(customerCumulationBatch.getSmsSed()));
            cols.add(StringUtil.toString(customerCumulationBatch.getSmsDny()));
            cols.add(StringUtil.toString(customerCumulationBatch.getSmsRmrk()));
            cols.add(StringUtil.toString(customerCumulationBatch.getSmsDesc()));
            cols.add(StringUtil.toString(customerCumulationBatch.getSetlAmt()));
            cols.add(StringUtil.toString(customerCumulationBatch.getTelNotCnt()));
            cols.add(StringUtil.toString(User.getUsNm(users, customerCumulationBatch.getRgstrId())));

            results.add(cols);
        }

        return results;
    }

    /**
     * <p>날짜 맵</p>
     *
     * @return ListOrderedMap
     */
    public static ListOrderedMap getYearMap() {
        ListOrderedMap result = new ListOrderedMap();
        int currYear = NumberUtil.toInt(DateUtil.getCurrentYear());

        for (int i = 0; i < currYear - MIN_YEAR + 1; i++) {
            int minYear_ = MIN_YEAR + i;
            result.put(StringUtil.toString(minYear_), StringUtil.toString(minYear_)+"년");
        }

        return result;
    }

    /**
     * <p>날짜(최근 6개월) 맵</p>
     *
     * @return ListOrderedMap
     */
    public static ListOrderedMap getYm6mMap() {
        ListOrderedMap result = new ListOrderedMap();
        String currDate = DateUtil.getCurrentDate();

        int minMonth = -5;
        for (int i = 0; i < 6; i++) {
            String ym_ = DateUtil.addMonths(currDate, minMonth + i);
            String ym = DateFormatUtil.format(ym_, "yyyyMM");
            String ym2 = DateFormatUtil.format(ym_, "yyyy년 MM월");
            result.put(ym, ym2);
        }

        return result;
    }

    /**
     * <p>toString (엑셀용)</p>
     *
     * @param str toString
     * @return 값
     */
    public String toString(String str) {
        if (StringUtil.isBlank(str)) {
            return "";
        }
        return str;
    }

    /**
     * <p>회원처리항목 체크</p>
     *
     * @param tp 구분
     * @return ListOrderedMap
     */
    public String getCustProcChk(String tp) {
        String result = "";

        if (StringUtil.contains(this.getCustDtl(), tp)) {
            return "V";
        }

        return result;
    }

    public String getVal(String nm) {
        String val = "";

        switch (nm) {
            case "gdNm" :
                if (this.getGoods() != null) {
                    val = this.getGoods().getNm();
                }
                break;
            case "rcvTp" :
                val = this.getRcvTp();
                break;
            case "rcvRmrk" :
                val = this.getRcvRmrk();
                break;
            case "fileTp" :
                val = this.getFileTp();
                break;
            default:
                break;
        }

        return StringUtil.toString(val);
    }

}