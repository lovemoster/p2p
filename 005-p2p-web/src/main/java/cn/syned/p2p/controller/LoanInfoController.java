package cn.syned.p2p.controller;

import cn.syned.p2p.constant.Constants;
import cn.syned.p2p.entity.*;
import cn.syned.p2p.service.BidInfoService;
import cn.syned.p2p.service.FinaceAccountService;
import cn.syned.p2p.service.LoanInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/loan")
public class LoanInfoController {

    @DubboReference(interfaceClass = LoanInfoService.class, version = "1.0.0", timeout = 20000)
    public LoanInfoService loanInfoService;

    @DubboReference(interfaceClass = BidInfoService.class, version = "1.0.0", timeout = 20000)
    public BidInfoService bidInfoService;

    @DubboReference(interfaceClass = FinaceAccountService.class, version = "1.0.0", timeout = 20000)
    FinaceAccountService finaceAccountService;

    /**
     * 查询产品信息
     *
     * @return 产品信息视图
     */
    @RequestMapping("/loan")
    public String loan(@RequestParam(name = "ptype", required = false) Integer ptype, Page page, Model model) {
        //查询产品信息
        List<LoanInfo> loanInfoList = loanInfoService.queryLoanInfoByTypeAndPage(ptype, page);
        Long loanCount = loanInfoService.queryLoanInfoByTypeAndPageCount(ptype);
        page.setTotalCount(loanCount);
        if (page.getPageContent() == null) {
            page.setPageContent(9);
        }
        if (page.getCunPage() == null) {
            page.setCunPage(1);
        }
        int totalPage;
        if (loanCount % page.getPageContent() == 0) {
            totalPage = loanCount.intValue() / page.getPageContent();
        } else {
            totalPage = loanCount.intValue() / page.getPageContent() + 1;
        }
        page.setTotalPage(totalPage);
        model.addAttribute(Constants.LOAN_INFO_LIST, loanInfoList);
        model.addAttribute("ptype", ptype);
        model.addAttribute("page", page);
        return "loan";
    }

    @RequestMapping("/loanInfo")
    public String loanInfo(@RequestParam("loanId") Integer loanId, Model model, HttpSession session) {
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(loanId);
        model.addAttribute("loanInfo", loanInfo);
        List<BidInfo> bidInfoList = bidInfoService.queryBidInfoByLoanId(loanId);
        model.addAttribute("bidInfoList", bidInfoList);
        User user = (User) session.getAttribute("user");
        if (user != null && user.getIdCard() != null) {
            FinaceAccount finaceAccount = finaceAccountService.queryFinaceAccountByUserId(user.getId());
            model.addAttribute("finaceAccount", finaceAccount);
        }
        return "loanInfo";
    }
}