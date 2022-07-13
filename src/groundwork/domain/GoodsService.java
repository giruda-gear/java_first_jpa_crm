package com.aaihc.crm.biz.groundwork.domain;

import asn.util.lang.StringUtil;
import com.aaihc.crm.core.domain.BaseEntityRgstTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>GoodsService (상품 서비스 Domain)</p>
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
@Entity(name = "t_gw_gds_svc")
@SequenceGenerator(name = "t_seq_gw_gds_svc_gen", sequenceName = "t_seq_gw_gds_svc", allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "rgstYmd", column = @Column(name = "gs_rgst_ymd"))
        , @AttributeOverride(name = "rgstHis", column = @Column(name = "gs_rgst_his"))
})
@ToString(exclude = {"service", "serviceDetail", "goods", "goodsServiceModifyHistories"})
public class GoodsService extends BaseEntityRgstTime {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_gw_gds_svc_gen")
    @Column(name = "gs_seq")
    private long seq; // 상품 서비스 일련번호
    @Column(name = "gd_cd")
    private String gdCd; // 상품코드
    @Column(name = "sd_seq")
    private long sdSeq; // 서비스 상세 일련번호
    @Column(name = "sv_seq")
    private long svSeq; // 서비스 일련번호
    @Column(name = "gs_rstct_cnt")
    private Integer rstctCnt; // 제한회수
    @Column(name = "gs_cnsl_cnt")
    private int cnslCnt; // 상담수

    @NotFound(action= NotFoundAction.IGNORE)
    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sv_seq", insertable = false, updatable = false)
    private Service service;    // 서비스

    @NotFound(action= NotFoundAction.IGNORE)
    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sd_seq", insertable = false, updatable = false)
    private ServiceDetail serviceDetail;    // 서비스 상세

    @JsonManagedReference
    @OneToMany(mappedBy = "goodsService")
    private List<GoodsServiceModifyHistory> goodsServiceModifyHistories = new ArrayList<>();   // 상품서비스수정이력

    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "gd_cd", insertable = false, updatable = false)
    private Goods goods;    // 상품

    @Override
    public boolean equals(Object o) {
        GoodsService that = (GoodsService) o;
        return StringUtil.equals(gdCd, that.gdCd) && sdSeq == that.sdSeq && svSeq == that.svSeq && rstctCnt == that.rstctCnt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gdCd, sdSeq, svSeq, rstctCnt);
    }

}