package com.aaihc.crm.web.groundwork;

import asn.util.colletion.MapUtil;
import asn.util.date.DateFormatUtil;
import asn.util.date.DateUtil;
import asn.util.lang.StringUtil;
import asn.util.num.NumberUtil;
import com.aaihc.crm.biz.counsel.domain.dto.CounselCallbackTotDto;
import com.aaihc.crm.biz.counsel.domain.dto.CounselCnslYmdDto;
import com.aaihc.crm.biz.counsel.domain.dto.CounselCoCdGdSeqCnslYmdDto;
import com.aaihc.crm.biz.counsel.domain.dto.CounselCuNmCnslYmdDto;
import com.aaihc.crm.biz.counsel.service.CounselCallbackService;
import com.aaihc.crm.biz.counsel.service.CounselService;
import com.aaihc.crm.biz.customer.domain.Customer;
import com.aaihc.crm.biz.customer.domain.CustomerMapping;
import com.aaihc.crm.biz.customer.domain.CustomerTfa;
import com.aaihc.crm.biz.customer.domain.dto.CustomerBcTpTfaYnDto;
import com.aaihc.crm.biz.customer.domain.dto.CustomerCoCdGdSeqDto;
import com.aaihc.crm.biz.customer.domain.dto.CustomerCoOfrMbrRgstYmdDto;
import com.aaihc.crm.biz.customer.domain.dto.CustomerSetlObjYnDto;
import com.aaihc.crm.biz.customer.service.CustomerService;
import com.aaihc.crm.biz.ems.domain.dto.MmsGrpReportDto;
import com.aaihc.crm.biz.ems.domain.dto.MmsReportDto;
import com.aaihc.crm.biz.ems.domain.dto.SmsGrpReportDto;
import com.aaihc.crm.biz.ems.domain.dto.SmsReportDto;
import com.aaihc.crm.biz.ems.service.MmsReportService;
import com.aaihc.crm.biz.ems.service.SmsReportService;
import com.aaihc.crm.biz.groundwork.domain.CommonCode;
import com.aaihc.crm.biz.groundwork.domain.Company;
import com.aaihc.crm.biz.groundwork.domain.Goods;
import com.aaihc.crm.biz.groundwork.service.CommonCodeService;
import com.aaihc.crm.biz.groundwork.service.CompanyService;
import com.aaihc.crm.biz.groundwork.service.GoodsService;
import com.aaihc.crm.biz.liaison.domain.CtiCallback;
import com.aaihc.crm.biz.liaison.service.CtiCallbackService;
import com.aaihc.crm.biz.liaison.service.CtiDidService;
import com.aaihc.crm.biz.log.domain.CustomerCumulationBatch;
import com.aaihc.crm.biz.log.domain.CustomerCumulationBatchGrpRnkDto;
import com.aaihc.crm.biz.log.domain.CustomerReadLogTp;
import com.aaihc.crm.biz.log.service.CustomerCumulationBatchService;
import com.aaihc.crm.biz.other.domain.dto.WebzineDto;
import com.aaihc.crm.biz.other.service.WebzineService;
import com.aaihc.crm.biz.security.domain.User;
import com.aaihc.crm.biz.security.domain.UserMenu;
import com.aaihc.crm.biz.security.service.UserMenuService;
import com.aaihc.crm.biz.security.service.UserService;
import com.aaihc.crm.core.config.ConfigProperty;
import com.aaihc.crm.core.domain.JsonResult;
import com.aaihc.crm.core.domain.Search;
import com.aaihc.crm.core.message.MessageProperty;
import com.aaihc.crm.web.WebBaseController;
import com.querydsl.core.types.Order;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 프로젝트명	: AAIHC 통합 CRM 개발
 * 개발사		: 어솔루션- <a href="http://www.asolution.biz/">asolution.biz</a>
 *
 * <p>CustomerCumulationBatchController (정산기초데이터 Controller)</p>
 *
 * @author : 김형수
 * date : 2021. 03. 09.
 * <p>
 * modifier 	:
 * modify-date  :
 * description  :
 */
@Controller
@RequestMapping("/groundwork/customer_cumulation_batch")
@RequiredArgsConstructor
public class CustomerCumulationBatchController extends WebBaseController {

    private final String VW_PFX_PATH = "/page/groundwork/customer_cumulation_batch";

    private final CustomerCumulationBatchService customerCumulationBatchService;

    private final CustomerService customerService;

    private final UserService userService;

    private final UserMenuService userMenuService;

    private final CommonCodeService commonCodeService;

    private final CompanyService companyService;

    private final GoodsService goodsService;

    private final CounselService counselService;

    private final CtiCallbackService ctiCallbackService;

    private final CtiDidService ctiDidService;

    private final CounselCallbackService counselCallbackService;

    private final SmsReportService smsReportService;

    private final MmsReportService mmsReportService;

    private final WebzineService webzineService;

    @ModelAttribute("map")
    private ModelMap initModel() {
        ModelMap result = new ModelMap();
        result.addAttribute("ccPrdMap", this.prdMap());
        result.addAttribute("ccDayMap", this.dayMap());
        result.addAttribute("ccDayStrMap", this.dayStrMap());
        result.addAttribute("ccHldyYnMap", this.hldyYnMap());
        result.addAttribute("ccDtlYnMap", this.dtlYnMap());

        result.addAttribute("ccYearMap", CustomerCumulationBatch.getYearMap());
        result.addAttribute("ccYm6mMap", CustomerCumulationBatch.getYm6mMap());
        result.addAttribute("ccFileCycMap", CustomerCumulationBatch.getFileCycMap());
        result.addAttribute("ccUseStopYnMap", Customer.getUseStopYnMap());
        result.addAttribute("ccSetlObjYnMap", Customer.getSetlObjYnMap());
        result.addAttribute("ccBatYnMap", Customer.getBatYnMap());
        result.addAttribute("ccCtUseYnMap", CustomerTfa.getUseYnMap());
        result.addAttribute("ccCmTpMap", CustomerMapping.getTpMap());
        result.addAttribute("ccCtiCallbackStatMap", CtiCallback.getStatusMap());
        result.addAttribute("ccCallbackTpMap", CounselCallbackTotDto.getTpMap());
        result.addAttribute("ccDutyTpMap", CounselCallbackTotDto.getDutyTpMap());

        return result;
    }

    /**
     * <p>목록 (정산기초데이터 설명)</p>
     *
     * @return 화면 html
     */
    @GetMapping("/description_list.do")
    public String descriptionList() {
        return VW_PFX_PATH + "/description_list";
    }

    /**
     * <p>목록 (B2B)</p>
     *
     * @param search  검색
     * @param model   model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/b2b_list.do")
    public String b2bList(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        String year = request.getParameter("year");
        if(StringUtil.isBlank(year)) {
            year = DateUtil.getCurrentYear();
        }
        search.add("year", year);

        String gdCd = request.getParameter("gdCd");
        search.add("gdCd", gdCd);

        String coCd = request.getParameter("coCd");
        search.add("coCd", coCd);

        if (StringUtil.isNotBlank(coCd)) {
            Search search1 = new Search();
            search1.add("sortFld", "nm");
            search1.add("sortMetd", "ASC");
            search1.add("coCd", coCd);
            List<Goods> goodses = goodsService.findList(search1);

            if (goodses.size() > 0) {
                model.addAttribute("goodses", Goods.ofJson(goodses));
            }
        }

        // 회원 요약
        Search search2 = new Search();
        search2.add("year", year);
        search2.add("coCd", coCd);
        search2.add("gdCd", gdCd);
        search2.add("custTretStrtYmd", request.getParameter("custTretStrtYmd"));
        search2.add("custTretEndYmd", request.getParameter("custTretEndYmd"));
        List<CustomerCumulationBatchGrpRnkDto> customerCumulationBatchGrpRnkDtos = customerCumulationBatchService.findListGrpRnk1(search2);
        CustomerCumulationBatchGrpRnkDto customerCumulationBatchGrpRnkDto = new CustomerCumulationBatchGrpRnkDto(customerCumulationBatchGrpRnkDtos);

        List<CustomerCumulationBatchGrpRnkDto> uniqueCustomerCumulationBatchGrpRnkDtos = customerCumulationBatchGrpRnkDto.getUniqueCustomerCumulationBatchGrpRnkDtos();

        // 회원 상세
        if (StringUtil.isNotBlank(coCd)) {
            model.addAttribute("customerDetails", customerService.findListGrpCoCdGdSeqSvcAct(search));
        }

        // 처리 기록
        String custTretStrtYmd = request.getParameter("custTretStrtYmd");
        String custTretEndYmd = request.getParameter("custTretEndYmd");

        Search search3 = new Search();
        search3.add("pg", request.getParameter("pg"), "1");
        search3.add("pgSz", request.getParameter("pgSz"), ConfigProperty.getString("page.size"));
        search3.add("gdCd", gdCd);
        search3.add("coCd", coCd);
        search3.add("custTretStrtYmd", custTretStrtYmd);
        search3.add("custTretEndYmd", custTretEndYmd);
        model.addAttribute("customerCumulationBatchPage", customerCumulationBatchService.findFJoinPage(search3));
        model.addAttribute("search3", search3);
        model.addAttribute("user", new User(userService.findList(new Search())));

        model.addAttribute("custTretStrtYmd", custTretStrtYmd);
        model.addAttribute("custTretEndYmd", custTretEndYmd);

        model.addAttribute("companies", companyService.findList(new Search()));
        model.addAttribute("customerCumulationBatchGrpRnkDto", customerCumulationBatchGrpRnkDto);
        model.addAttribute("uniqueCustomerCumulationBatchGrpRnkDtos", uniqueCustomerCumulationBatchGrpRnkDtos);

        List<UserMenu> userMenus = userMenuService.findList(new Search("usSeq", NumberUtil.toLong(super.getSecurityUser().getSeq()), "mnSeq", 25L));
        if (userMenus.size() == 1) {
            model.addAttribute("userMenu", userMenus.get(0));
        }

        return VW_PFX_PATH + "/b2b_list";
    }

    /**
     * <p>목록 (계열사)</p>
     *
     * @param search  검색
     * @param model   model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/subsidiary_list.do")
    public String subsidiaryList(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        String year = request.getParameter("year");
        if(StringUtil.isBlank(year)) {
            year = DateUtil.getCurrentYear();
        }
        search.add("year", year);

        String gdCd = request.getParameter("gdCd");
        search.add("gdCd", gdCd);
        String coCd = request.getParameter("coCd");
        search.add("coCd", coCd);
        String coUseYn = request.getParameter("coUseYn");
        search.add("coUseYn", coUseYn);
        String batchYn = request.getParameter("batchYn");
        search.add("batchYn", batchYn);
        String tfaYn = request.getParameter("tfaYn");
        search.add("tfaYn", tfaYn);
        String cmTp = request.getParameter("cmTp");
        search.add("cmTp", cmTp);
        String coOfrMbrRgstStrtYmd = request.getParameter("coOfrMbrRgstStrtYmd");
        search.add("coOfrMbrRgstStrtYmd", coOfrMbrRgstStrtYmd);
        String coOfrMbrRgstEndYmd = request.getParameter("coOfrMbrRgstEndYmd");
        search.add("coOfrMbrRgstEndYmd", coOfrMbrRgstEndYmd);

        // 상품 목록
        Search search1 = new Search();
        if (StringUtil.isNotBlank(coCd)) {
            search1.add("coCd", coCd);
        } else {
            search1.add("coCds", Company.AAI_CDS);
        }

        search1.add("sortFld", "nm");
        search1.add("sortMetd", "ASC");
        List<Goods> goodses = goodsService.findList(search1);

        if (goodses.size() > 0) {
            model.addAttribute("goodses", goodses);
        }
        if (StringUtil.isNotBlank(coCd)) {
            model.addAttribute("goodsJson", Goods.ofJson(goodses));
        }

        // 회원 요약
        Search search2 = new Search();
        search2.add("coOfrMbrRgstYear", year);
        search2.add("coCd", coCd);
        search2.add("gdCd", gdCd);
        search2.add("coUseYn", coUseYn);
        search2.add("batchYn", batchYn);
        search2.add("tfaYn", tfaYn);
        search2.add("cmTp", cmTp);
        search2.add("coOfrMbrRgstStrtYmd", coOfrMbrRgstStrtYmd);
        search2.add("coOfrMbrRgstEndYmd", coOfrMbrRgstEndYmd);
        List<CustomerCoOfrMbrRgstYmdDto> customerCoOfrMbrRgstYmdDtos = customerService.findListGrpCoOfrMbrRgstYmd(search2);
        CustomerCoOfrMbrRgstYmdDto customerCoOfrMbrRgstYmdDto = new CustomerCoOfrMbrRgstYmdDto(customerCoOfrMbrRgstYmdDtos);
        List<CustomerCoOfrMbrRgstYmdDto> uCustomerCoOfrMbrRgstYmdDtos = customerCoOfrMbrRgstYmdDto.getUniqueCustomerCoOfrMbrRgstYmdDtos();
        model.addAttribute("customerCoOfrMbrRgstYmdDto", customerCoOfrMbrRgstYmdDto);
        model.addAttribute("uCustomerCoOfrMbrRgstYmdDtos", uCustomerCoOfrMbrRgstYmdDtos);

        // 회원 상세
        Search search3 = new Search();
        search3.add("coCd", coCd);
        search3.add("coCd", coCd);
        search3.add("gdCd", gdCd);
        search3.add("coUseYn", coUseYn);
        search3.add("batchYn", batchYn);
        search3.add("tfaYn", tfaYn);
        search3.add("cmTp", cmTp);
        search3.add("coOfrMbrRgstStrtYmd", coOfrMbrRgstStrtYmd);
        search3.add("coOfrMbrRgstEndYmd", coOfrMbrRgstEndYmd);
        model.addAttribute("customerDetails", customerService.findListGrpBcTpTfaYn(search3));
        model.addAttribute("customerBcTpTfaYnDto", new CustomerBcTpTfaYnDto());

        model.addAttribute("custTretStrtYmd", request.getParameter("custTretStrtYmd"));
        model.addAttribute("custTretEndYmd", request.getParameter("custTretEndYmd"));
        model.addAttribute("companies", companyService.findList(new Search("cds", Company.AAI_CDS)));

        return VW_PFX_PATH + "/subsidiary_list";
    }

    /**
     * <p>목록 (전체회원)</p>
     *
     * @param search  검색
     * @param model   model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/whole_customer_list.do")
    public String wholeCustomerList(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        String coCd = request.getParameter("coCd");
        search.add("coCd", coCd);
        search.add("gdCd", request.getParameter("gdCd"));
        search.add("sortFld", "company.cd");
        search.add("sortMetd", Order.ASC);
        search.add("sortFld2", "goods.cd");
        search.add("sortMetd2", Order.ASC);

        String setlObjYn = request.getParameter("setlObjYn");
        String useStopYn = request.getParameter("useStopYn");

        search.add("setlObjYn", setlObjYn);
        search.add("useStopYn", useStopYn);

        List<Company> companies = companyService.findList(new Search("sortFld", "nm", "sortMetd", Order.ASC));
        model.addAttribute("companies", companies);

        Search search1 = new Search();
        if (StringUtil.isNotBlank(coCd)) {
            search1.add("coCd", coCd);
        }
        search1.add("sortFld", "nm");
        search1.add("sortMetd", Order.ASC);
        List<Goods> goodses = goodsService.findList(search1);

        if (goodses.size() > 0) {
            model.addAttribute("goodses", goodses);
        }
        if (StringUtil.isNotBlank(coCd)) {
            model.addAttribute("goodsJson", Goods.ofJson(goodses));
        }

        List<CustomerSetlObjYnDto> customerSetlObjYnDtos = customerService.findListGrpSetlObjYn(search);

        List<CustomerCoCdGdSeqDto> customerCoCdGdSeqDtos = customerService.findListGrpCocdGdSeq(search);

        CustomerSetlObjYnDto customerSetlObjYnDto = new CustomerSetlObjYnDto(companies, goodses, customerSetlObjYnDtos, customerCoCdGdSeqDtos);
        List<CustomerSetlObjYnDto> customerSetlObjYnDtos1 = customerSetlObjYnDto.merge();

        model.addAttribute("customerSummaries", customerSetlObjYnDtos1);
        model.addAttribute("sums", CustomerSetlObjYnDto.getSums(customerSetlObjYnDtos1));

        return VW_PFX_PATH + "/whole_customer_list";
    }

    /**
     * <p>목록 (서비스 상담)</p>
     *
     * @param search 검색
     * @param days 요일
     * @param model model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/service_counsel_list.do")
    public String serviceCounselList(@ModelAttribute("search") Search search, @RequestParam(value = "days", required = false) List<String> days, ModelMap model, HttpServletRequest request) {
        String coCd = request.getParameter("coCd");
        String gdCd = request.getParameter("gdCd");

        List<Company> companies = companyService.findList(new Search("sortFld", "nm", "sortMetd", Order.ASC));
        model.addAttribute("companies", companies);

        Search search1 = new Search();
        if (StringUtil.isNotBlank(coCd)) {
            search1.add("coCd", coCd);
        }
        search1.add("sortFld", "nm");
        search1.add("sortMetd", Order.ASC);
        List<Goods> goodses = goodsService.findList(search1);

        if (goodses.size() > 0) {
            model.addAttribute("goodses", goodses);
        }
        if (StringUtil.isNotBlank(coCd)) {
            model.addAttribute("goodsJson", Goods.ofJson(goodses));
        }

        String usId = request.getParameter("usId");
        String cnslCat = request.getParameter("cnslCat");
        String cnslCatDetail = request.getParameter("cnslCatDetail");
        String cnslCatDetail2 = request.getParameter("cnslCatDetail2");
        String cnslCat1 = "";

        if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isBlank(cnslCatDetail2)) {
            cnslCat1 = cnslCatDetail;
        } else if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isNotBlank(cnslCatDetail2)) {
            cnslCat1 = cnslCatDetail + "-" + cnslCatDetail2;
        } else {
            cnslCat1 = cnslCat;
        }

        search.add("coCd", coCd);
        search.add("gdCd", gdCd);
        search.add("skillGrp", request.getParameter("skillGrp"));
        search.add("custTp", request.getParameter("custTp"));
        search.add("cnslSvcTp", request.getParameter("cnslSvcTp"));
        search.add("cnslExtnlTp", request.getParameter("cnslExtnlTp"));
        search.add("cnslCat", cnslCat1);
        search.add("cnslTretRslt", request.getParameter("cnslTretRslt"));
        search.add("cnslStfdg", request.getParameter("cnslStfdg"));
        search.add("cuNm", request.getParameter("cuNm"));
        search.add("cnslStrtYmd", request.getParameter("cnslStrtYmd"), DateUtil.getCurrentDate());
        search.add("cnslEndYmd", request.getParameter("cnslEndYmd"), DateUtil.getCurrentDate());
        search.add("prd", request.getParameter("prd"));
        search.add("days", days);
        search.add("hldyYn", request.getParameter("hldyYn"));
        search.add("usId", usId);

        List<CounselCnslYmdDto> counselCnslYmdDtos = counselService.findListGrpCnslYmd(search);

        if (days != null) {
            model.addAttribute("days", days.toArray());
        }

        String timeTableYn = request.getParameter("timeTableYn");

        Search search2 = new Search();
        search2.add("sortFld", "korNm");
        search2.add("sortMetd", Order.ASC);
        search2.add("cnslUseYn", MapUtil.getKey(User.getCnslUseYnMap(), "사용"));
        List<User> users = userService.findList(search2);

        int colspanCnt = 1;
        if (StringUtil.isNotBlank(usId)) {
            Optional<User> optional = users.stream().filter(p -> (StringUtil.equals(p.getId(), usId))).findFirst();
            if (optional.isPresent()) {
                model.addAttribute("usNm", optional.get().getKorNm());
                colspanCnt++;
            }
        }
        if (StringUtil.isNotBlank(coCd)) {
            Optional<Company> optional = companies.stream().filter(p -> (StringUtil.equals(p.getCd(), coCd))).findFirst();
            if (optional.isPresent()) {
                model.addAttribute("coNm", optional.get().getNm());
                colspanCnt++;
            }
        }
        if (StringUtil.isNotBlank(gdCd)) {
            Optional<Goods> optional = goodses.stream().filter(p -> StringUtil.equals(p.getCd(), gdCd)).findFirst();
            if (optional.isPresent()) {
                model.addAttribute("gdNm", optional.get().getNm());
                colspanCnt++;
            }
        }

        if (StringUtil.isNotBlank(cnslCat)) {
            colspanCnt++;
        }
        if (StringUtil.isNotBlank(cnslCatDetail)) {
            colspanCnt++;
        }
        if (StringUtil.isNotBlank(cnslCatDetail2)) {
            colspanCnt++;
        }

        model.addAttribute("colspanCnt", colspanCnt);
        model.addAttribute("timeTableYn", timeTableYn);
        model.addAttribute("counselCnslYmds", counselCnslYmdDtos);
        model.addAttribute("sums", CounselCnslYmdDto.getSums(counselCnslYmdDtos, timeTableYn));
        model.addAttribute("users", users);
        model.addAttribute("cnslCat", cnslCat);
        model.addAttribute("cnslCatDetail", cnslCatDetail);
        model.addAttribute("cnslCatDetail2", cnslCatDetail2);
        model.addAttribute("cnslDetailText", request.getParameter("cnslDetailText"));
        model.addAttribute("cnslDetailText2", request.getParameter("cnslDetailText2"));

        getCommonCodes(model);

        return VW_PFX_PATH + "/service_counsel_list";
    }

    /**
     * <p>목록 (서비스 상담)</p>
     *
     * @param search 검색
     * @param days 요일
     * @param model model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/service_customer_list.do")
    public String serviceCustomerList(@ModelAttribute("search") Search search, @RequestParam(value = "days", required = false) List<String> days, ModelMap model, HttpServletRequest request) {
        String coCd = request.getParameter("coCd");
        String gdCd = request.getParameter("gdCd");

        List<Company> companies = companyService.findList(new Search("sortFld", "nm", "sortMetd", Order.ASC));
        model.addAttribute("companies", companies);

        Search search1 = new Search();
        if (StringUtil.isNotBlank(coCd)) {
            search1.add("coCd", coCd);
        }
        search1.add("sortFld", "nm");
        search1.add("sortMetd", Order.ASC);
        List<Goods> goodses = goodsService.findList(search1);

        if (goodses.size() > 0) {
            model.addAttribute("goodses", goodses);
        }
        if (StringUtil.isNotBlank(coCd)) {
            model.addAttribute("goodsJson", Goods.ofJson(goodses));
        }

        String prd = request.getParameter("prd");
        String usId = request.getParameter("usId");
        String cnslCat = request.getParameter("cnslCat");
        String cnslCatDetail = request.getParameter("cnslCatDetail");
        String cnslCatDetail2 = request.getParameter("cnslCatDetail2");
        String cnslCat1 = "";

        if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isBlank(cnslCatDetail2)) {
            cnslCat1 = cnslCatDetail;
        } else if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isNotBlank(cnslCatDetail2)) {
            cnslCat1 = cnslCatDetail + "-" + cnslCatDetail2;
        } else {
            cnslCat1 = cnslCat;
        }

        search.add("coCd", coCd);
        search.add("gdCd", gdCd);
        search.add("skillGrp", request.getParameter("skillGrp"));
        search.add("custTp", request.getParameter("custTp"));
        search.add("cnslSvcTp", request.getParameter("cnslSvcTp"));
        search.add("cnslExtnlTp", request.getParameter("cnslExtnlTp"));
        search.add("cnslCat", cnslCat1);
        search.add("cnslTretRslt", request.getParameter("cnslTretRslt"));
        search.add("cnslStfdg", request.getParameter("cnslStfdg"));
        search.add("cuNm", request.getParameter("cuNm"));
        search.add("cnslStrtYmd", request.getParameter("cnslStrtYmd"), DateUtil.getCurrentDate());
        search.add("cnslEndYmd", request.getParameter("cnslEndYmd"), DateUtil.getCurrentDate());
        search.add("prd", prd);
        search.add("days", days);
        search.add("hldyYn", request.getParameter("hldyYn"));
        search.add("usId", usId);

        List<CounselCuNmCnslYmdDto> counselCuNmCnslYmdDtos = counselService.findListGrpCuNmCnslYmd(search);

        if (days != null) {
            model.addAttribute("days", days.toArray());
        }

        Search search2 = new Search();
        search2.add("sortFld", "korNm");
        search2.add("sortMetd", Order.ASC);
        search2.add("cnslUseYn", MapUtil.getKey(User.getCnslUseYnMap(), "사용"));
        List<User> users = userService.findList(search2);

        int colspanCnt = 2;
        if (StringUtil.isNotBlank(usId)) {
            Optional<User> optional = users.stream().filter(p -> (StringUtil.equals(p.getId(), usId))).findFirst();
            if (optional.isPresent()) {
                model.addAttribute("usNm", optional.get().getKorNm());
                colspanCnt++;
            }
        }
        if (StringUtil.isNotBlank(coCd)) {
            Optional<Company> optional = companies.stream().filter(p -> (StringUtil.equals(p.getCd(), coCd))).findFirst();
            if (optional.isPresent()) {
                model.addAttribute("coNm", optional.get().getNm());
                colspanCnt++;
            }
        }
        if (StringUtil.isNotBlank(gdCd)) {
            Optional<Goods> optional = goodses.stream().filter(p -> StringUtil.equals(p.getCd(), gdCd)).findFirst();
            if (optional.isPresent()) {
                model.addAttribute("gdNm", optional.get().getNm());
                colspanCnt++;
            }
        }

        String dtlYn = request.getParameter("dtlYn");
        if (StringUtil.isBlank(dtlYn)) {
            dtlYn = "N";
        }
        if (StringUtil.equals(dtlYn, "Y")) {
            colspanCnt++;
        }
        if (StringUtil.isNotBlank(cnslCat)) {
            colspanCnt++;
        }
        if (StringUtil.isNotBlank(cnslCatDetail)) {
            colspanCnt++;
        }
        if (StringUtil.isNotBlank(cnslCatDetail2)) {
            colspanCnt++;
        }

        model.addAttribute("dtlYn", dtlYn);
        model.addAttribute("colspanCnt", colspanCnt);
        model.addAttribute("counselCustomers", counselCuNmCnslYmdDtos);
        model.addAttribute("sums", CounselCuNmCnslYmdDto.getSums(counselCuNmCnslYmdDtos, prd));
        model.addAttribute("users", users);
        model.addAttribute("cnslCat", cnslCat);
        model.addAttribute("cnslCatDetail", cnslCatDetail);
        model.addAttribute("cnslCatDetail2", cnslCatDetail2);
        model.addAttribute("cnslDetailText", request.getParameter("cnslDetailText"));
        model.addAttribute("cnslDetailText2", request.getParameter("cnslDetailText2"));

        getCommonCodes(model);

        return VW_PFX_PATH + "/service_customer_list";
    }

    /**
     * <p>목록 (서비스 이용)</p>
     *
     * @param search 검색
     * @param model model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/service_use_list.do")
    public String serviceUseList(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        String coCd = request.getParameter("coCd");
        String gdCd = request.getParameter("gdCd");

        List<Company> companies = companyService.findList(new Search("sortFld", "nm", "sortMetd", Order.ASC));
        model.addAttribute("companies", companies);

        Search search1 = new Search();
        if (StringUtil.isNotBlank(coCd)) {
            search1.add("coCd", coCd);
        }
        search1.add("sortFld", "nm");
        search1.add("sortMetd", Order.ASC);
        List<Goods> goodses = goodsService.findList(search1);

        if (goodses.size() > 0) {
            model.addAttribute("goodses", goodses);
        }
        if (StringUtil.isNotBlank(coCd)) {
            model.addAttribute("goodsJson", Goods.ofJson(goodses));
        }

        Search search2 = new Search();
        search2.add("sortFld", "korNm");
        search2.add("sortMetd", Order.ASC);
        search2.add("cnslUseYn", MapUtil.getKey(User.getCnslUseYnMap(), "사용"));
        model.addAttribute("users", userService.findList(search2));

        String cnslCat = request.getParameter("cnslCat");
        String cnslCatDetail = request.getParameter("cnslCatDetail");
        String cnslCatDetail2 = request.getParameter("cnslCatDetail2");
        String cnslCat1 = "";

        if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isBlank(cnslCatDetail2)) {
            cnslCat1 = cnslCatDetail;
        } else if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isNotBlank(cnslCatDetail2)) {
            cnslCat1 = cnslCatDetail + "-" + cnslCatDetail2;
        } else {
            cnslCat1 = cnslCat;
        }

        search.add("csYear", request.getParameter("csYear"), DateUtil.getCurrentYear());
        search.add("coCd", coCd);
        search.add("gdCd", gdCd);
        search.add("skillGrp", request.getParameter("skillGrp"));
        search.add("custTp", request.getParameter("custTp"));
        search.add("cnslSvcTp", request.getParameter("cnslSvcTp"));
        search.add("cnslExtnlTp", request.getParameter("cnslExtnlTp"));
        search.add("cnslCat", cnslCat1);
        search.add("cnslTretRslt", request.getParameter("cnslTretRslt"));
        search.add("cnslStfdg", request.getParameter("cnslStfdg"));
        search.add("cuNm", request.getParameter("cuNm"));
        search.add("usId", request.getParameter("usId"));

        if (StringUtil.isNotBlank(coCd)) {
            List<CounselCoCdGdSeqCnslYmdDto> counselCoCdGdSeqDtos = counselService.findListGrpCoCdGdSeqCnslYmd(search);
            CounselCoCdGdSeqCnslYmdDto counselCoCdGdSeqCnslYmdDto = new CounselCoCdGdSeqCnslYmdDto(counselCoCdGdSeqDtos);
            Map<String, List<CounselCoCdGdSeqCnslYmdDto>> counselSummaryGroupBy = counselCoCdGdSeqCnslYmdDto.getCounselCoCdGdSeqCnslYmdDtosGrpByCoCd();

            CounselCoCdGdSeqCnslYmdDto cnslSmry1 = new CounselCoCdGdSeqCnslYmdDto(counselSummaryGroupBy.get(MapUtil.getKey(Company.getCatgMap(), "생명보험")));
            CounselCoCdGdSeqCnslYmdDto cnslSmry2 = new CounselCoCdGdSeqCnslYmdDto(counselSummaryGroupBy.get(MapUtil.getKey(Company.getCatgMap(), "손해보험")));
            CounselCoCdGdSeqCnslYmdDto cnslSmry3 = new CounselCoCdGdSeqCnslYmdDto(counselSummaryGroupBy.get(MapUtil.getKey(Company.getCatgMap(), "일반회사")));

            cnslSmry1.setTotCntSums(cnslSmry1.getSums());
            cnslSmry2.setTotCntSums(cnslSmry2.getSums());
            cnslSmry3.setTotCntSums(cnslSmry3.getSums());

            model.addAttribute("cnslSmry1", cnslSmry1);
            model.addAttribute("cnslSmry2", cnslSmry2);
            model.addAttribute("cnslSmry3", cnslSmry3);
        }

        getCommonCodes(model);
        model.addAttribute("cnslCat", cnslCat);
        model.addAttribute("cnslCatDetail", cnslCatDetail);
        model.addAttribute("cnslCatDetail2", cnslCatDetail2);

        return VW_PFX_PATH + "/service_use_list";
    }

    /**
     * <p>목록 (서비스 콜백)</p>
     *
     * @param search 검색
     * @param days 요일
     * @param model model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/service_callback_list.do")
    public String serviceCallbackList(@ModelAttribute("search") Search search, @RequestParam(value = "days", required = false) List<String> days, ModelMap model, HttpServletRequest request) {
        String tp = request.getParameter("tp");
        String stat = request.getParameter("stat");
        String cid = request.getParameter("cid");
        String agent = request.getParameter("agent"); // 요청자
        String callbackAgent = request.getParameter("callbackAgent"); // 처리자
        String company = request.getParameter("company");
        String prd = request.getParameter("prd");
        String dtlYn = request.getParameter("dtlYn");
        String duty = request.getParameter("duty");

        if (StringUtil.isBlank(prd)) {
            prd = MapUtil.getKey(this.prdMap(), "일별");
        }
        if (StringUtil.isBlank(dtlYn)) {
            dtlYn = "Y";
        }

        // 상담원
        Search search2 = new Search();
        search2.add("sortFld", "korNm");
        search2.add("sortMetd", Order.ASC);
        search2.add("cnslUseYn", MapUtil.getKey(User.getCnslUseYnMap(), "사용"));
        List<User> users = userService.findList(search2);
        model.addAttribute("users", users);

        search.add("tp", tp);
        search.add("strtYmd", request.getParameter("strtYmd"), DateUtil.getCurrentDate());
        search.add("endYmd", request.getParameter("endYmd"), DateUtil.getCurrentDate());
        search.add("cid", cid);
        search.add("stat", stat);
        search.add("agent", agent);
        search.add("callbackAgent", callbackAgent);
        search.add("days", days);
        search.add("prd", prd);
        search.add("company", company);
        search.add("duty", duty);

        // cti 콜백 조회
        List<CounselCallbackTotDto> ctiCallbacks = null;
        if (StringUtil.isBlank(tp) || StringUtil.equals(tp, MapUtil.getKey(CounselCallbackTotDto.getTpMap(), "IVR"))) {
            ctiCallbacks  = ctiCallbackService.findListLJoinCidUserCu(search);
        }

        // 상담 콜백 조회
        List<CounselCallbackTotDto> callbacks = null;
        if (StringUtil.isBlank(tp) || StringUtil.equals(tp, MapUtil.getKey(CounselCallbackTotDto.getTpMap(), "CRM"))) {
            callbacks = counselCallbackService.findListLJoinCnslUserCuGw(search);
        }

        // 각각의 콜백 조회내역 merge
        List<CounselCallbackTotDto> counselCallbackTots = new ArrayList<>();
        if (ctiCallbacks != null) {
            counselCallbackTots.addAll(ctiCallbacks);
        }
        if (callbacks != null) {
            counselCallbackTots.addAll(callbacks);
        }

        Comparator sorted = Comparator.comparing(CounselCallbackTotDto::getTrmsdtTm).reversed();
        List<CounselCallbackTotDto> counselCallbackTotDtos = (List<CounselCallbackTotDto>) counselCallbackTots.stream().sorted(sorted).collect(Collectors.toList());

        if (StringUtil.equals(dtlYn, "Y")) {
            model.addAttribute("counselCallbackTot", new CounselCallbackTotDto(counselCallbackTotDtos));
        } else {
            String grpTp = null;
            if (StringUtil.isNotBlank(agent)) {
                grpTp = "req";
            }
            if (StringUtil.isNotBlank(callbackAgent)) {
                grpTp = "proc";
            }
            CounselCallbackTotDto counselCallbackTotDto = new CounselCallbackTotDto(counselCallbackTotDtos);
            List<CounselCallbackTotDto> counselCallbackTotDtos1 = counselCallbackTotDto.getListGrpBy(prd, grpTp, users);
            model.addAttribute("counselCallbackTotGrpBy", new CounselCallbackTotDto(counselCallbackTotDtos1));
        }

        model.addAttribute("companies", companyService.findList(new Search("useYn", "Y", "sortFld", "nm", "sortMetd", Order.ASC)));
        model.addAttribute("tp", tp);
        model.addAttribute("prd", prd);
        model.addAttribute("dtlYn", dtlYn);

        return VW_PFX_PATH + "/service_callback_list";
    }

    /**
     * <p>목록 (서비스 문자)</p>
     *
     * @param search  검색
     * @param model   model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/service_message_list.do")
    public String serviceMessageList(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        search.add("ym", request.getParameter("ym"), DateFormatUtil.format(DateUtil.getCurrentDate(), "yyyyMM"));
        search.add("callback", request.getParameter("callback"));

        List<SmsGrpReportDto> smsRprts = smsReportService.findGrpListRprt(search);
        List<SmsGrpReportDto> smsRprtDtls = smsReportService.findGrpListRprtDtl(search);

        List<MmsGrpReportDto> mmsRprts = mmsReportService.findGrpList(search);
        List<MmsGrpReportDto> mmsRprtDtls = mmsReportService.findGrpListDtl(search);

        model.addAttribute("smsRprts", smsRprts);
        model.addAttribute("smsRprtDtls", smsRprtDtls);
        model.addAttribute("mmsRprts", mmsRprts);
        model.addAttribute("mmsRprtDtls", mmsRprtDtls);

        model.addAttribute("ctiDids", ctiDidService.findList(new Search("useYn", "Y", "sortFld", "didDescription", "sortMetd", Order.ASC)));

        return VW_PFX_PATH + "/service_message_list";
    }

    /**
     * <p>목록 (서비스 MCATALOG)</p>
     *
     * @param search 검색
     * @param model model
     * @param request request
     * @return 화면 html
     */
    @GetMapping("/service_mcatalog_list.do")
    public String serviceMcatalogList(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        String callback = request.getParameter("callback");

        search.add("ym", request.getParameter("ym"), DateFormatUtil.format(DateUtil.getCurrentDate(), "yyyyMM"));
        search.add("callback", callback);

        List<WebzineDto> webzineDtos = webzineService.findList(search);
        List<SmsReportDto> smsReportDtos = smsReportService.findList(search);
        List<MmsReportDto> mmsReportDtos = mmsReportService.findList(search);

        model.addAttribute("webzineDto", new WebzineDto(webzineDtos, smsReportDtos, mmsReportDtos));
        model.addAttribute("companyMap", companyService.findMap("Y"));
        model.addAttribute("ctiDids", ctiDidService.findList(new Search("useYn", "Y", "sortFld", "didDescription", "sortMetd", Order.ASC)));

        return VW_PFX_PATH + "/service_mcatalog_list";
    }

    /**
     * <p>엑셀 다운로드 (B2B 회원요약)</p>
     *
     * @param search 검색
     * @param model model
     * @param request request
     * @return ModelAndView
     */
    @GetMapping(value = "/customer_summary_excel_download.do", headers = "Accept=application/vnd.ms-excel")
    public ModelAndView customerSummaryExcelDownload(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        String year = request.getParameter("year");
        if (StringUtil.isBlank(year)) {
            year = DateUtil.getCurrentYear();
        }

        search.add("year", year);
        search.add("coCd", request.getParameter("coCd"));
        search.add("gdCd", request.getParameter("gdCd"));
        search.add("custTretStrtYmd", request.getParameter("custTretStrtYmd"));
        search.add("custTretEndYmd", request.getParameter("custTretEndYmd"));

        List<CustomerCumulationBatchGrpRnkDto> customerCumulationBatchGrpRnkDtos = customerCumulationBatchService.findListGrpRnk1(search);
        CustomerCumulationBatchGrpRnkDto customerCumulationBatchGrpRnkDto = new CustomerCumulationBatchGrpRnkDto(customerCumulationBatchGrpRnkDtos);

        List<CustomerCumulationBatchGrpRnkDto> uniqueCustomerCumulationBatchGrpRnkDtos = customerCumulationBatchGrpRnkDto.getUniqueCustomerCumulationBatchGrpRnkDtos();

        model.addAttribute("templatePath", "/static/excel/groundwork/customer_cumulation_batch/b2b/customer_summary");
        model.addAttribute("filename", "정산기초데이터_회원요약" + year);
        model.addAttribute("custSmries", uniqueCustomerCumulationBatchGrpRnkDtos);
        model.addAttribute("custSmryDto", customerCumulationBatchGrpRnkDto);
        model.addAttribute("year", year);
        model.addAttribute("m01", year+"01");
        model.addAttribute("m02", year+"02");
        model.addAttribute("m03", year+"03");
        model.addAttribute("m04", year+"04");
        model.addAttribute("m05", year+"05");
        model.addAttribute("m06", year+"06");
        model.addAttribute("m07", year+"07");
        model.addAttribute("m08", year+"08");
        model.addAttribute("m09", year+"09");
        model.addAttribute("m10", year+"10");
        model.addAttribute("m11", year+"11");
        model.addAttribute("m12", year+"12");

        super.setCustomerReadLog(null, null, CustomerReadLogTp.download, uniqueCustomerCumulationBatchGrpRnkDtos.size());

        return new ModelAndView("jxlsView", model);
    }

    /**
     * <p>엑셀 다운로드 (B2B 처리기록)</p>
     *
     * @param search 검색
     * @param model model
     * @param request request
     * @return ModelAndView
     */
    @GetMapping(value = "/process_record_excel_download.do", headers = "Accept=application/vnd.ms-excel")
    public ModelAndView processRecordExcelDownload(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        try {
            search.add("coCd", request.getParameter("coCd"));
            search.add("gdCd", request.getParameter("gdCd"));
            search.add("custTretStrtYmd", request.getParameter("custTretStrtYmd"));
            search.add("custTretEndYmd", request.getParameter("custTretEndYmd"));

            List<User> users = userService.findList(new Search());
            List<CustomerCumulationBatch> customerCumulationBatches = customerCumulationBatchService.findFJoinList(search);
            List<List<String>> contRows = CustomerCumulationBatch.ofExcel(customerCumulationBatches, users);

            model.addAttribute("filename", "개인정보처리대장_" + DateUtil.getCurrentDate());
            model.addAttribute("colNms", CustomerCumulationBatch.getExcelColNms());
            model.addAttribute("contRows", contRows);

            super.setCustomerReadLog(null, null, CustomerReadLogTp.download, customerCumulationBatches.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ModelAndView("sxssfView", model);
    }

    /**
     * <p>엑셀 다운로드 (서비스상담)</p>
     *
     * @param search 검색
     * @param days 요일
     * @param model model
     * @param request request
     * @return ModelAndView
     */
    @GetMapping(value = "/counsel_record_excel_download.do", headers = "Accept=application/vnd.ms-excel")
    public ModelAndView counselRecordExcelDownload(@ModelAttribute("search") Search search, @RequestParam(value = "days", required = false) List<String> days, ModelMap model, HttpServletRequest request) {
        String usId = request.getParameter("usId");
        String prd = request.getParameter("prd");
        String coCd = request.getParameter("coCd");
        String gdCd = request.getParameter("gdCd");
        String cnslCat = request.getParameter("cnslCat");
        String cnslCatNm1 = request.getParameter("cnslCatNm1");
        String cnslCatNm2 = request.getParameter("cnslCatNm2");
        String cnslCatNm3 = request.getParameter("cnslCatNm3");

        String cnslCatDetail = request.getParameter("cnslCatDetail");
        String cnslCatDetail2 = request.getParameter("cnslCatDetail2");

        if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isBlank(cnslCatDetail2)) {
            cnslCat = cnslCatDetail;
        } else if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isNotBlank(cnslCatDetail2)) {
            cnslCat = cnslCatDetail + "-" + cnslCatDetail2;
        }

        search.add("coCd", coCd);
        search.add("gdCd", gdCd);
        search.add("skillGrp", request.getParameter("skillGrp"));
        search.add("custTp", request.getParameter("custTp"));
        search.add("cnslSvcTp", request.getParameter("cnslSvcTp"));
        search.add("cnslExtnlTp", request.getParameter("cnslExtnlTp"));
        search.add("cnslCat", cnslCat);
        search.add("cnslTretRslt", request.getParameter("cnslTretRslt"));
        search.add("cnslStfdg", request.getParameter("cnslStfdg"));
        search.add("cuNm", request.getParameter("cuNm"));
        search.add("cnslStrtYmd", request.getParameter("cnslStrtYmd"), DateUtil.getCurrentDate());
        search.add("cnslEndYmd", request.getParameter("cnslEndYmd"), DateUtil.getCurrentDate());
        search.add("prd", prd);
        search.add("days", days);
        search.add("hldyYn", request.getParameter("hldyYn"));
        search.add("usId", usId);

        List<CounselCnslYmdDto> counselCnslYmdDtos = counselService.findListGrpCnslYmd(search);

        String timeTableYn = request.getParameter("timeTableYn");

        List<User> users = userService.findList(new Search("cnslUseYn", MapUtil.getKey(User.getCnslUseYnMap(), "사용")));
        List<Company> companies = companyService.findList(new Search());
        List<Goods> goodses = goodsService.findList(new Search());

        String coNm = "";
        String gdNm = "";
        String usNm = "";

        if (StringUtil.isNotBlank(coCd)) {
            Optional<Company> optional = companies.stream().filter(p -> (StringUtil.equals(p.getCd(), coCd))).findFirst();
            if (optional.isPresent()) {
                coNm = optional.get().getNm();
            }
        }

        if (StringUtil.isNotBlank(gdCd)) {
            Optional<Goods> optional = goodses.stream().filter(p -> StringUtil.equals(p.getCd(), gdCd)).findFirst();
            if (optional.isPresent()) {
                gdNm = optional.get().getNm();
            }
        }

        if (StringUtil.isNotBlank(usId)) {
            Optional<User> optional = users.stream().filter(p -> (StringUtil.equals(p.getId(), usId))).findFirst();
            if (optional.isPresent()) {
                usNm = optional.get().getKorNm();
            }
        }

        model.addAttribute("timeTableYn", timeTableYn);

        model.addAttribute("filename", "서비스상담_" + DateUtil.getCurrentDate());
        model.addAttribute("colNms", CounselCnslYmdDto.getExcelColNms(coNm, gdNm, usNm, cnslCatNm1, cnslCatNm2, cnslCatNm3, timeTableYn));
        model.addAttribute("contRows",  CounselCnslYmdDto.ofExcel(counselCnslYmdDtos, coNm, gdNm, usNm, cnslCatNm1, cnslCatNm2, cnslCatNm3, timeTableYn));

        super.setCustomerReadLog(null, null, CustomerReadLogTp.download, counselCnslYmdDtos.size());

        return new ModelAndView("sxssfView", model);
    }

    /**
     * <p>엑셀 다운로드 (서비스회원)</p>
     *
     * @param search 검색
     * @param days 요일
     * @param model model
     * @param request request
     * @return ModelAndView
     */
    @GetMapping(value = "/customer_record_excel_download.do", headers = "Accept=application/vnd.ms-excel")
    public ModelAndView customerRecordExcelDownload(@ModelAttribute("search") Search search, @RequestParam(value = "days", required = false) List<String> days, ModelMap model, HttpServletRequest request) {
        String coCd = request.getParameter("coCd");
        String gdCd = request.getParameter("gdCd");
        String usId = request.getParameter("usId");
        String prd = request.getParameter("prd");
        String cnslCat = request.getParameter("cnslCat");
        String cnslCatDetail = request.getParameter("cnslCatDetail");
        String cnslCatDetail2 = request.getParameter("cnslCatDetail2");
        String cnslCatNm1 = request.getParameter("cnslCatNm1");
        String cnslCatNm2 = request.getParameter("cnslCatNm2");
        String cnslCatNm3 = request.getParameter("cnslCatNm3");

        List<User> users = userService.findList(new Search("cnslUseYn", MapUtil.getKey(User.getCnslUseYnMap(), "사용")));
        List<Company> companies = companyService.findList(new Search());
        List<Goods> goodses = goodsService.findList(new Search());

        String coNm = "";
        String gdNm = "";
        String usNm = "";

        if (StringUtil.isNotBlank(coCd)) {
            Optional<Company> optional = companies.stream().filter(p -> (StringUtil.equals(p.getCd(), coCd))).findFirst();
            if (optional.isPresent()) {
                coNm = optional.get().getNm();
            }
        }

        if (StringUtil.isNotBlank(gdCd)) {
            Optional<Goods> optional = goodses.stream().filter(p -> StringUtil.equals(p.getCd(), gdCd)).findFirst();
            if (optional.isPresent()) {
                gdNm = optional.get().getNm();
            }
        }

        if (StringUtil.isNotBlank(usId)) {
            Optional<User> optional = users.stream().filter(p -> (StringUtil.equals(p.getId(), usId))).findFirst();
            if (optional.isPresent()) {
                usNm = optional.get().getKorNm();
            }
        }

        String cnslCat1 = "";

        if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isBlank(cnslCatDetail2)) {
            cnslCat1 = cnslCatDetail;
        } else if (StringUtil.isNotBlank(cnslCatDetail) && StringUtil.isNotBlank(cnslCatDetail2)) {
            cnslCat1 = cnslCatDetail + "-" + cnslCatDetail2;
        } else {
            cnslCat1 = cnslCat;
        }

        search.add("coCd", coCd);
        search.add("gdCd", gdCd);
        search.add("skillGrp", request.getParameter("skillGrp"));
        search.add("custTp", request.getParameter("custTp"));
        search.add("cnslSvcTp", request.getParameter("cnslSvcTp"));
        search.add("cnslExtnlTp", request.getParameter("cnslExtnlTp"));
        search.add("cnslCat", cnslCat1);
        search.add("cnslTretRslt", request.getParameter("cnslTretRslt"));
        search.add("cnslStfdg", request.getParameter("cnslStfdg"));
        search.add("cuNm", request.getParameter("cuNm"));
        search.add("cnslStrtYmd", request.getParameter("cnslStrtYmd"), DateUtil.getCurrentDate());
        search.add("cnslEndYmd", request.getParameter("cnslEndYmd"), DateUtil.getCurrentDate());
        search.add("prd", prd);
        search.add("days", days);
        search.add("hldyYn", request.getParameter("hldyYn"));
        search.add("usId", usId);

        List<CounselCuNmCnslYmdDto> counselCuNmCnslYmdDtos = counselService.findListGrpCuNmCnslYmd(search);


        String dtlYn = request.getParameter("dtlYn");

        model.addAttribute("filename", "서비스회원_" + DateUtil.getCurrentDate());
        model.addAttribute("colNms", CounselCuNmCnslYmdDto.getExcelColNms(coNm, gdNm, usNm, dtlYn, cnslCatNm1, cnslCatNm2, cnslCatNm3, prd));
        model.addAttribute("contRows",  CounselCuNmCnslYmdDto.ofExcel(counselCuNmCnslYmdDtos, coNm, gdNm, usNm, dtlYn, cnslCatNm1, cnslCatNm2, cnslCatNm3, prd));

        super.setCustomerReadLog(null, null, CustomerReadLogTp.download, counselCuNmCnslYmdDtos.size());

        return new ModelAndView("sxssfView", model);
    }

    /**
     * <p>엑셀 다운로드 (서비스콜백)</p>
     *
     * @param search 검색
     * @param model model
     * @param request request
     * @return ModelAndView
     */
    @GetMapping(value = "/callback_record_excel_download.do", headers = "Accept=application/vnd.ms-excel")
    public ModelAndView callbackRecordExcelDownload(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        String stat = request.getParameter("stat");
        String cid = request.getParameter("cid");
        String tp = request.getParameter("tp");
        String agent = request.getParameter("agent");
        String callbackAgent = request.getParameter("callbackAgent");

        search.add("tp", tp);
        search.add("strtYmd", request.getParameter("strtYmd"), DateUtil.getCurrentDate());
        search.add("endYmd", request.getParameter("endYmd"), DateUtil.getCurrentDate());
        search.add("cid", cid);
        search.add("stat", stat);
        search.add("agent", agent);
        search.add("callbackAgent", callbackAgent);

        // cti 콜백 조회
        List<CounselCallbackTotDto> ctiCallbacks = null;
        if (StringUtil.isBlank(tp) || StringUtil.equals(tp, MapUtil.getKey(CounselCallbackTotDto.getTpMap(), "IVR"))) {
            ctiCallbacks  = ctiCallbackService.findListLJoinCidUserCu(search);
        }

        // 상담 콜백 조회
        List<CounselCallbackTotDto> callbacks = null;
        if (StringUtil.isBlank(tp) || StringUtil.equals(tp, MapUtil.getKey(CounselCallbackTotDto.getTpMap(), "CRM"))) {
            callbacks = counselCallbackService.findListLJoinCnslUserCuGw(search);
        }

        // 각각의 콜백 조회내역 merge
        List<CounselCallbackTotDto> counselCallbackTots = new ArrayList<>();
        if (ctiCallbacks != null) {
            counselCallbackTots.addAll(ctiCallbacks);
        }
        if (callbacks != null) {
            counselCallbackTots.addAll(callbacks);
        }

        Comparator sorted = Comparator.comparing(CounselCallbackTotDto::getTrmsdtTm).reversed();
        List<CounselCallbackTotDto> counselCallbackTotDtos2 = (List<CounselCallbackTotDto>) counselCallbackTots.stream().sorted(sorted).collect(Collectors.toList());

        model.addAttribute("filename", "콜백_" + DateUtil.getCurrentDate());
        model.addAttribute("colNms", CounselCallbackTotDto.getExcelColNms());
        model.addAttribute("contRows",  CounselCallbackTotDto.ofExcel(counselCallbackTotDtos2));

        super.setCustomerReadLog(null, null, CustomerReadLogTp.download, counselCallbackTotDtos2.size());

        return new ModelAndView("sxssfView", model);
    }

    /**
     * <p>엑셀 다운로드 (서비스 MCATALOG)</p>
     *
     * @param search 검색
     * @param model model
     * @param request request
     * @return ModelAndView
     */
    @GetMapping(value = "/mcatalog_record_excel_download.do", headers = "Accept=application/vnd.ms-excel")
    public ModelAndView mcatalogRecordExcelDownload(@ModelAttribute("search") Search search, ModelMap model, HttpServletRequest request) {
        String ym = request.getParameter("ym");
        search.add("ym", ym);
        search.add("callback", request.getParameter("callback"));

        List<WebzineDto> webzineDtos = webzineService.findList(search);
        List<SmsReportDto> smsReportDtos = smsReportService.findList(search);
        List<MmsReportDto> mmsReportDtos = mmsReportService.findList(search);

        model.addAttribute("webzineDto", new WebzineDto(webzineDtos, smsReportDtos, mmsReportDtos));
        model.addAttribute("templatePath", "/static/excel/groundwork/customer_cumulation_batch/service_mcatalog/mcatalog_record");
        model.addAttribute("filename", "MCATALOG_" + ym);
        model.addAttribute("dateFormatUtil", DateFormatUtil.class);
        model.addAttribute("dateFormat1", "yyyyMMddHHmmss");
        model.addAttribute("dateFormat2", "yyyy.MM.dd");
        model.addAttribute("companyMap", companyService.findMap("Y"));

        super.setCustomerReadLog(null, null, CustomerReadLogTp.download, webzineDtos.size());

        return new ModelAndView("jxlsView", model);
    }

    /**
     * <p>고객사별 상품 목록</p>
     *
     * @param coCd 고객사 코드
     * @return 모델뷰(jsonView)
     */
    @PostMapping(value = "/goods_list_json.do", headers = "Accept=application/json")
    public ModelAndView goodsListJson(@RequestParam("coCd") String coCd) {
        JsonResult result = new JsonResult();
        List<Goods> goodses = null;

        try {
            if (StringUtil.isNotBlank(coCd) && !StringUtil.equals(coCd, "9999")) {
                Search search = new Search();
                search.add("sortFld", "nm");
                search.add("sortMetd", "ASC");
                search.add("coCd", coCd);
                goodses = goodsService.findList(search);
            }

            if (goodses != null && goodses.size() > 0) {
                result.add("goodses", Goods.ofJson(goodses));
            }

        } catch (Exception e) {
            result.setResult(-99, MessageProperty.getMsg("system.alert.error"), e.getMessage());
        }

        return new ModelAndView("jsonView", result);
    }

    /**
     * <p>상담유형 목록</p>
     *
     * @param cnslCat  공통코드
     * @param depth  깊이
     * @return 모델뷰 (jsonView)
     */
    @PostMapping(value="/service_detail_list_json.do", headers = "Accept=application/json")
    public ModelAndView serviceListJson(@RequestParam("cnslCat") String cnslCat, @RequestParam(value = "depth", required = false) Integer depth) {
        JsonResult result = new JsonResult();

        try {
            Search search = new Search();
            search.add("sortFld","seq");
            search.add("sortMetd","ASC");
            search.add("cat", cnslCat);
            search.add("useYn", "Y");

            if (depth != null && depth == 3) {
                search.add("depth", 3);
            }

            List<CommonCode> commonCodes = commonCodeService.findList(search);

            result.add("commonCodes", commonCodes);

        } catch (Exception e) {
            result.setResult(-99, MessageProperty.getMsg("system.alert.error"), e.getMessage());
        }

        return new ModelAndView("jsonView", result);
    }

    /**
     * <p>공통코드를 Model에 담습니다.</p>
     *
     * @param model model
     */
    private void getCommonCodes(ModelMap model) {
        List<CommonCode> commonCodes = commonCodeService.findList(new Search("sortFld", "cd", "sortMetd", Order.ASC));
        CommonCode commonCode = new CommonCode(commonCodes);

        model.addAttribute("skillGroupCdMap", commonCode.getCatCdMap("0101", "Y"));
        model.addAttribute("cnslSvcTpCdMap", commonCode.getCatCdMap("0021", "Y"));
        model.addAttribute("custTpCdMap", commonCode.getCatCdMap("0011", "Y"));
        model.addAttribute("cnslExtnlTpCdMap", commonCode.getCatCdMap("0051", "Y"));
        model.addAttribute("cnslCatCdMap", commonCode.getCatCdMap("0001", "Y"));
        model.addAttribute("cnslTretRsltCdMap", commonCode.getCatCdMap("0031", "Y"));
        model.addAttribute("cnslStfdgCdMap", commonCode.getCatCdMap("0071", "Y"));
    }

    /**
     * <p>기간 검색값(연,월,일별)을 Map 형태로 가져옵니다.</p>
     *
     * @return 맵
     */
    private ListOrderedMap prdMap() {
        ListOrderedMap results = new ListOrderedMap();
        results.put("day", "일별");
        results.put("month", "월별");
        results.put("year", "연별");

        return results;
    }

    /**
     * <p>요일을 Map 형태로 가져옵니다.</p>
     *
     * @return 맵
     */
    private ListOrderedMap dayMap() {
        ListOrderedMap results = new ListOrderedMap();
        results.put("1", "일");
        results.put("2", "월");
        results.put("3", "화");
        results.put("4", "수");
        results.put("5", "목");
        results.put("6", "금");
        results.put("7", "토");

        return results;
    }

    /**
     * <p>요일을 Map 형태로 가져옵니다.</p>
     *
     * @return 맵
     */
    private ListOrderedMap dayStrMap() {
        ListOrderedMap results = new ListOrderedMap();
        results.put("일", "일");
        results.put("월", "월");
        results.put("화", "화");
        results.put("수", "수");
        results.put("목", "목");
        results.put("금", "금");
        results.put("토", "토");

        return results;
    }

    /**
     * <p>공휴일 포함여부 Map 형태로 가져옵니다.</p>
     *
     * @return 맵
     */
    private ListOrderedMap hldyYnMap() {
        ListOrderedMap results = new ListOrderedMap();
        results.put("Y", "공휴일");
        results.put("N", "공휴일 제외");

        return results;
    }

    /**
     * <p>상세 Map 형태로 가져옵니다.</p>
     *
     * @return 맵
     */
    private ListOrderedMap dtlYnMap() {
        ListOrderedMap results = new ListOrderedMap();
        results.put("Y", "상세");
        results.put("N", "요약");

        return results;
    }

}
