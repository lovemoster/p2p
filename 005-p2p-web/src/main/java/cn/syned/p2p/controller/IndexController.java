package cn.syned.p2p.controller;

import cn.syned.p2p.constant.Constants;
import cn.syned.p2p.service.BidInfoService;
import cn.syned.p2p.service.LoanInfoService;
import cn.syned.p2p.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class IndexController {

    @DubboReference(interfaceClass = LoanInfoService.class, version = "1.0.0", timeout = 20000)
    public LoanInfoService loanInfoService;

    @DubboReference(interfaceClass = UserService.class, version = "1.0.0", timeout = 20000)
    public UserService userService;

    @DubboReference(interfaceClass = BidInfoService.class, version = "1.0.0", timeout = 20000)
    public BidInfoService bidInfoService;

    @RequestMapping(path = {"/", "/index"})
    public String toIndex(Model model) {
        //查询历史年化收益率
        Double historyRate = loanInfoService.queryHistoryRate();
        model.addAttribute(Constants.HISTORY_RATE, historyRate);
        //查询平台用户数
        Long userCount = userService.queryAllUserCount();
        model.addAttribute(Constants.USER_COUNT, userCount);
        //查询累计成交额
        Double historyTurnover = bidInfoService.queryHistoryTurnover();
        model.addAttribute(Constants.HISTORY_TURNOVER, historyTurnover);
        //查询参数
        HashMap<String, Object> queryParameter = new HashMap<>();
        //新手宝
        queryParameter.put("type", 0);
        queryParameter.put("startWith", 0);
        queryParameter.put("endWith", 1);
        model.addAttribute(Constants.NOVICE_TREASURE_LIST, loanInfoService.queryLoanInfoByType(queryParameter));
        //优选产品
        queryParameter.put("type", 1);
        queryParameter.put("startWith", 0);
        queryParameter.put("endWith", 4);
        model.addAttribute(Constants.PREFERRED_PRODUCT_LIST, loanInfoService.queryLoanInfoByType(queryParameter));
        //散标产品
        queryParameter.put("type", 2);
        queryParameter.put("startWith", 0);
        queryParameter.put("endWith", 8);
        model.addAttribute(Constants.BULK_STANDARD_PRODUCT_LIST, loanInfoService.queryLoanInfoByType(queryParameter));
        return "index";
    }

}
