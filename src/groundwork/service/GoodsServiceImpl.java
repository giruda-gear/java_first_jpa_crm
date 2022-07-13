package com.aaihc.crm.biz.groundwork.service;

import asn.util.date.DateUtil;
import asn.util.lang.StringUtil;
import asn.util.num.NumberUtil;
import com.aaihc.crm.biz.groundwork.domain.Goods;
import com.aaihc.crm.biz.groundwork.repository.GoodsRepository;
import com.aaihc.crm.biz.groundwork.validation.GoodsValidator;
import com.aaihc.crm.core.domain.BaseDomain;
import com.aaihc.crm.core.domain.Search;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>GoodsServiceImpl (상품 Service Implement)</p>
 *
 * @author 	    : 김형수
 * date 		: 2021. 03. 11.
 *
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodsServiceService goodsServiceService;
    private GoodsValidator goodsValidator;

    @Autowired
    public void setGoodsValidator(GoodsValidator goodsValidator) {
        this.goodsValidator = goodsValidator;
    }

    /**
     * <p>상품을 등록합니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#add(Goods)
     * @param goods 상품
     * @return 수
     */
    public String add(Goods goods) {
        String result = "";

        // 유효성 확인
        goodsValidator.add(goods);
        String cd = goodsRepository.findCd(); // 코드 자동생성
        goods.setCd(cd);

        if (StringUtil.equals(goods.getSvcOfrPrd(), "100")) { // 종신
            goods.setHlcrEndYmd(DateUtil.addYears(goods.getHlcrStrtYmd(), NumberUtil.toInt(goods.getSvcOfrPrd())));
        } else {
            goods.setHlcrEndYmd(DateUtil.addMonths(goods.getHlcrStrtYmd(), NumberUtil.toInt(goods.getSvcOfrPrd())));
        }

        goods.setGdsSvcCnt(goods.getGoodsServices().size());

        goodsRepository.save(goods);

        result = goods.getCd();

        // 상품 서비스 저장
        goodsServiceService.save(result, goods.getGoodsServices());

        return result;
    }

    /**
     * <p>지정된 상품을 수정합니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#modify(Goods goods)
     * @param goods 상품
     * @return 수
     */
    public long modify(Goods goods) {
        long result = 0;
        goods.setMbrInfoItm();

        // 유효성 확인
        goodsValidator.modify(goods);

        Goods orgGoods = this.findOne(goods.getCd());
        if (orgGoods != null) {
            orgGoods.setCoCd(goods.getCoCd());
            orgGoods.setNm(goods.getNm());
            orgGoods.setSaleMthd(goods.getSaleMthd());
            orgGoods.setExclsNo(goods.getExclsNo());
            orgGoods.setObjr(goods.getObjr());
            orgGoods.setObjrDtl(goods.getObjrDtl());
            orgGoods.setAplyWrtYn(goods.getAplyWrtYn());
            orgGoods.setDtlCont(goods.getDtlCont());
            orgGoods.setPculr(goods.getPculr());
            orgGoods.setCtrtYmd(goods.getCtrtYmd());
            orgGoods.setCtrtStrtYmd(goods.getCtrtStrtYmd());
            orgGoods.setCtrtEndYmd(goods.getCtrtEndYmd());
            orgGoods.setHlcrStrtYmd(goods.getHlcrStrtYmd());
            orgGoods.setHlcrEndYmd(goods.getHlcrEndYmd());
            orgGoods.setSvcOfrPrd(goods.getSvcOfrPrd());
            orgGoods.setSvcOfrSchdl(goods.getSvcOfrSchdl());
            orgGoods.setMbrUpldSchdl(goods.getMbrUpldSchdl());
            orgGoods.setMbrInfoItm(goods.getMbrInfoItm());
            orgGoods.setMbrCancSchdl(goods.getMbrCancSchdl());
            orgGoods.setMbrDesc(goods.getMbrDesc());
            orgGoods.setCtrtAmt(goods.getCtrtAmt());
            orgGoods.setCtrtUseYn(goods.getCtrtUseYn());
            orgGoods.setUseYn(goods.getUseYn());
            orgGoods.setGdsSvcCnt(goods.getGoodsServices().size());
            orgGoods.setModr(goods.getModr());
            orgGoods.setModYmd(BaseDomain.getCurrYmd());
            orgGoods.setModHis(BaseDomain.getCurrHis());

            // 상품 서비스 저장 (수정, 삭제 포함)
            for (com.aaihc.crm.biz.groundwork.domain.GoodsService goodsService : goods.getGoodsServices()) {
                goodsService.setGdCd(goods.getCd());
            }
            goodsServiceService.save(orgGoods.getCd(), goods.getGoodsServices());

            result = 1;
        }

        return result;
    }

    /**
     * <p>지정된 상품을 삭제합니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#remove(String)
     * @param cd 코드
     * @return 수
     */
    public long remove(String cd) {
        long result = 0;
        if (StringUtil.isNotBlank(cd)) {
            goodsValidator.remove(cd);

            goodsRepository.deleteById(cd);

            result = 1;
        }

        return result;
    }

    /**
     * <p>지정된 상품을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#findOne(String)
     * @param cd 상품코드
     * @return 상품
     */
    @Transactional(readOnly = true)
    public Goods findOne(String cd) {
        return goodsRepository.findById(cd);
    }

    /**
     * <p>지정된 상품의 수를 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#findTotalCnt(Search)
     * @param search 검색
     * @return 수
     */
    @Transactional(readOnly = true)
    public long findTotalCnt(Search search) {
        return goodsRepository.count(search);
    }

    /**
     * <p>상품의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#findList(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<Goods> findList(Search search) {
        return goodsRepository.findAll(search);
    }

    /**
     * <p>상품의 목록을 가져옵니다 (페치조인)</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#findFJoinList(Search)
     * @param search 검색
     * @return 목록
     */
    @Transactional(readOnly = true)
    public List<Goods> findFJoinList(Search search) {
        search.add("fthJnYn", "Y");
        search.add("fthJnEntt1", "company");
        return goodsRepository.findAll(search);
    }

    /**
     * <p>상품의 목록을 가져옵니다</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#findPage(Search)
     * @param search 검색
     * @return 페이징 목록
     */
    @Transactional(readOnly = true)
    public Page<Goods> findPage(Search search) {
        return goodsRepository.findPage(search);
    }

    /**
     * <p>상품의 목록을 가져옵니다 (패치조인)</p>
     *
     * @see com.aaihc.crm.biz.groundwork.service.GoodsService#findFJoinPage(Search) 
     * @param search 검색
     * @return 페이징 목록
     */
    @Transactional(readOnly = true)
    public Page<Goods> findFJoinPage(Search search) {
        search.add("fthJnYn", "Y");
        search.add("fthJnEntt1", "company");
        return this.findPage(search);
    }

    /**
     * <p>상품의 목록을 맵 형태로 가져옵니다</p>
     *
     * @param useYn 사용여부
     * @return 목록
     */
    public ListOrderedMap findMap(String useYn) {
        Search search = new Search();

        if (StringUtil.isNotBlank(useYn)) {
            search.add("useYn", useYn);
        }

        return Goods.getMap((this.findList(search)));
    }

}