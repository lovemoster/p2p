package cn.syned.p2p.controller;

import cn.syned.p2p.entity.LoanInfo;
import cn.syned.p2p.entity.ResultVo;
import cn.syned.p2p.entity.User;
import cn.syned.p2p.enums.BidEnum;
import cn.syned.p2p.exception.BidException;
import cn.syned.p2p.service.BidInfoService;
import cn.syned.p2p.service.LoanInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 投资控制层
 */
@Controller
public class BidController {

    @DubboReference(interfaceClass = LoanInfoService.class, version = "1.0.0", timeout = 20000)
    public LoanInfoService loanInfoService;

    @DubboReference(interfaceClass = BidInfoService.class, version = "1.0.0", timeout = 20000)
    public BidInfoService bidInfoService;

    /**
     * 投资业务
     *
     * @param loanId   投资的产品ID
     * @param bidMoney 投资的金额
     * @param session  当前会话
     * @return 结果视图
     */
    @RequestMapping("/loan/bidInfo/invest")
    @ResponseBody
    public ResultVo invest(@RequestParam(name = "loanId", required = true) Integer loanId,
                           @RequestParam(name = "bidMoney", required = true) Double bidMoney,
                           HttpSession session) {
        ResultVo vo = new ResultVo();
        //从session中获取User
        User user = (User) session.getAttribute("user");
        //判断用户是否登录
        if (user == null) {
            vo.setCode(BidEnum.BID_USER_NO_LOGIN.getCode());
            vo.setMessage(BidEnum.BID_USER_NO_LOGIN.getMessage());
            return vo;
        }
        //判断用户是否实名认证
        if (user.getIdCard() == null) {
            vo.setCode(BidEnum.BID_USER_NO_REAL_NAME.getCode());
            vo.setMessage(BidEnum.BID_USER_NO_REAL_NAME.getMessage());
            return vo;
        }
        //根据产品ID查询产品信息
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(loanId);
        /*
         * 判断投资金额是否合法
         *
         * - 判断投资产品是否满标
         * - 判断投资金额是否小于等于0
         * - 判断投资金额是否为100的整数倍
         * - 判断投资金额是否小于最低投资金额
         * - 判断投资金额是否大于最高投资金额
         * - 判断投资金额是否大于剩余可投金额
         *
         */
        if (loanInfo.getProductMoney() <= 0) {
            vo.setCode(BidEnum.BID_FULL_MARK.getCode());
            vo.setMessage(BidEnum.BID_FULL_MARK.getMessage());
            return vo;
        }
        if (bidMoney <= 0) {
            vo.setCode(BidEnum.BID_MONEY_ILLEGAL.getCode());
            vo.setMessage(BidEnum.BID_MONEY_ILLEGAL.getMessage());
            return vo;
        }
        if (!(bidMoney % 100 == 0)) {
            vo.setCode(BidEnum.BID_MONEY_MUST_BE_MULTIPLE.getCode());
            vo.setMessage(BidEnum.BID_MONEY_MUST_BE_MULTIPLE.getMessage());
            return vo;
        }
        if (bidMoney < loanInfo.getBidMinLimit()) {
            vo.setCode(BidEnum.BID_MONEY_MIN_LIMIT.getCode());
            vo.setMessage(BidEnum.BID_MONEY_MIN_LIMIT.getMessage());
            return vo;
        }
        if (bidMoney > loanInfo.getBidMaxLimit()) {
            vo.setCode(BidEnum.BID_MONEY_MAX_LIMIT.getCode());
            vo.setMessage(BidEnum.BID_MONEY_MAX_LIMIT.getMessage());
            return vo;
        }
        if (bidMoney > loanInfo.getLeftProductMoney()) {
            vo.setCode(BidEnum.BID_MONEY_EXCEEDING_THE_REMAINING.getCode());
            vo.setMessage(BidEnum.BID_MONEY_EXCEEDING_THE_REMAINING.getMessage());
            return vo;
        }

        /*
         * 投资：
         * - 账户金额减少，判断是否满足投资金额
         * - 产品剩余可投金额减少，会不会小于投资金额
         * - 如果产品剩余可投金额==0，满标（1）
         * - 插入投资记录
         */

        //添加查询参数
        Map<String, Object> parasMap = new HashMap<>();
        parasMap.put("uid", user.getId());
        parasMap.put("loanId", loanId);
        parasMap.put("bidMoney", bidMoney);
        try {
            bidInfoService.invest(parasMap);
            vo.setCode(BidEnum.BID_INVEST_SUCCESS.getCode());
            vo.setMessage(BidEnum.BID_INVEST_SUCCESS.getMessage());
        } catch (BidException e) {
            vo.setCode(e.getCode());
            vo.setMessage(e.getMessage());
        }
        return vo;
    }
}
