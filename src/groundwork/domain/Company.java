package com.aaihc.crm.biz.groundwork.domain;

import asn.util.lang.StringUtil;
import com.aaihc.crm.biz.customer.domain.CustomerApproachControl;
import com.aaihc.crm.biz.liaison.domain.dto.CtiDidDto;
import com.aaihc.crm.biz.log.domain.CustomerCumulationBatch;
import com.aaihc.crm.core.domain.BaseEntityTime;
import com.aaihc.crm.core.domain.BaseRgstrNm;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>Company (고객사 Domain)</p>
 *
 * @author      : 김형수
 * date 		: 2021. 03. 08.
 * <p>
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Entity(name = "t_gw_co")
@SequenceGenerator(name = "t_seq_gw_co_gen", sequenceName = "t_seq_gw_co", allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "rgstYmd", column = @Column(name = "co_rgst_ymd"))
        , @AttributeOverride(name = "rgstHis", column = @Column(name = "co_rgst_his"))
        , @AttributeOverride(name = "modYmd", column = @Column(name = "co_mod_ymd"))
        , @AttributeOverride(name = "modHis", column = @Column(name = "co_mod_his"))
})
@ToString(exclude = {"goodses", "customerCumulationBatches", "customerApproachControls"})
public class Company extends BaseEntityTime {
    private static final long serialVersionUID = 1L;

    public static String[] AAI_CDS = new String[]{"A00", "A01", "A10"};

    @Id
    @Column(name = "co_cd", length = 3)
    private String cd; // 고객사 코드
    @Column(name = "co_nm", length = 500)
    private String nm; // 고객사명
    @Column(name = "co_use_yn", length = 1)
    private String useYn; // 사용여부
    @Column(name = "co_gds_cnt")
    private int gdsCnt; // 상품수
    @Column(name = "co_cust_rgst_cnt")
    private int custRgstCnt; // 고객등록수
    @Column(name = "co_cust_extnc_cnt")
    private int custExtncCnt; // 고객소멸수
    @Column(name = "co_cust_curr_cnt")
    private int custCurrCnt; // 고객현재수
    @Column(name = "co_cust_ascl_day")
    private int custAsclDay; // 고객 접근제어일 (소멸후)
    @Column(name = "co_cust_destr_day")
    private int custDestrDay; // 고객 파기일 (접근제어 후)

    @Transient
    private String nmWhCop; // 제휴여부 포함 명

    @CreatedBy
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "co_rgstr_id"))
            , @AttributeOverride(name = "nm", column = @Column(name = "co_rgstr_nm"))
    })
    private BaseRgstrNm rgstr;

    @CreatedBy
    // @LastModifiedBy
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "co_modr_id"))
            , @AttributeOverride(name = "nm", column = @Column(name = "co_modr_nm"))
    })
    private BaseRgstrNm modr;

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    private List<Goods> goodses = new ArrayList<>(); // 상품

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    private List<CustomerCumulationBatch> customerCumulationBatches = new ArrayList<>(); // 고객 누적 배치

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    private List<CustomerApproachControl> customerApproachControls = new ArrayList<>(); // 고객접근제어

    @Transient
    List<Company> companies = new ArrayList<>();

    @Transient
    List<CtiDidDto> ctiDids = new ArrayList<>();

    /**
     * <p>고객사 목록을 맵</p>
     *
     * @param companies 고객사 목록
     * @return 맵
     */
    public static ListOrderedMap getMap(List<Company> companies) {
        ListOrderedMap result = new ListOrderedMap();

        for (Company company : companies) {
            result.put(company.getCd(), company.getNm());
        }

        return result;
    }

    /**
     * <p>고객사 분류 맵</p>
     *
     * @return ListOrderedMap
     */
    public static ListOrderedMap getCatgMap() {
        ListOrderedMap result = new ListOrderedMap();
        result.put("L", "생명보험");
        result.put("N", "손해보험");
        result.put("E", "일반회사");

        return result;
    }

    /**
     * <p>고객사 목록(상품 포함)을 가져옵니다</p>
     *
     * @param companies 고객사 목록
     * @param goodses   상품 목록
     * @return 맵
     */
    public static List<Company> asList(List<Company> companies, List<Goods> goodses) {
        List<Company> result = new ArrayList<>();

        if (companies != null && goodses != null) {
            Map<String, List<Goods>> groupBy = goodses.stream().collect(Collectors.groupingBy(p -> (p.getCoCd()), TreeMap::new, Collectors.toList()));

            for (Company company : companies) {
                Company company_ = new Company();
                company_.setCd(company.getCd());
                company_.setNm(company.getNm());

                List<Goods> goods_ = groupBy.get(company.getCd());
                company_.setGoodses(goods_);
                result.add(company_);
            }
        }

        return result;
    }

    public String getNmWhCop() {
        String val = "";

        if (StringUtil.equals("Y", this.getUseYn())) {
            val = "[제휴] " + this.getNm();
        } else if (StringUtil.equals("N", this.getUseYn())) {
            val = "[비제휴] " + this.getNm();
        } else {
            val = "[제휴 확인] " + this.getNm();
        }

        return val;
    }

    /**
     * <p> 회사 코드로 이름을 구합니다.</p>
     *
     * @param companies 목록
     * @param coCd 아이디
     * @return 값
     */
    public static String getCoNm(List<Company> companies, String coCd) {
        Company company = new Company();
        Optional<Company> optional = companies.stream().filter(p -> StringUtil.equals(p.getCd(), coCd)).findFirst();

        if (optional.isPresent()) {
            company = optional.get();
        }

        return company.getNm();
    }
}