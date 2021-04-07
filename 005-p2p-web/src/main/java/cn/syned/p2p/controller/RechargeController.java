package cn.syned.p2p.controller;

import cn.syned.p2p.entity.RechargeRecord;
import cn.syned.p2p.entity.ResultVo;
import cn.syned.p2p.entity.User;
import cn.syned.p2p.enums.RechargeEnum;
import cn.syned.p2p.service.RechargeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 充值业务控制层
 */
@Controller
public class RechargeController {

    @DubboReference(interfaceClass = RechargeService.class, version = "1.0.0", timeout = 20000)
    RechargeService rechargeService;

    /**
     * 跳转充值页面
     *
     * @return 充值页面
     */
    @GetMapping("/loan/page/toRecharge")
    public String toRecharge() {
        return "toRecharge";
    }

    /**
     * 跳转充值记录
     *
     * @return 充值记录页面
     */
    @GetMapping("/loan/myRecharge")
    public String toMyRecharge(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        //查询该用户所有的充值记录
        List<RechargeRecord> rechargeRecordList = rechargeService.queryRechargeRecordByUID(user.getId());
        model.addAttribute("rechargeRecordList", rechargeRecordList);
        return "myRecharge";
    }

    /**
     * 充值业务
     *
     * @param paymentMethod 支付方式
     * @param rechargeMoney 充值金额
     * @param request       当前会话请求
     * @return null
     */
    @PostMapping("/loan/toRecharge")
    @ResponseBody
    public ResultVo recharge(@RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam("rechargeMoney") Double rechargeMoney,
                             HttpServletRequest request) {
        ResultVo vo = new ResultVo();
        //从session中获取用户
        User user = (User) request.getSession().getAttribute("user");
        //判断用户是否登录
        if (user == null) {
            vo.setCode(RechargeEnum.RECHARGE_USER_NO_LOGIN.getCode());
            vo.setMessage(RechargeEnum.RECHARGE_USER_NO_LOGIN.getMessage());
            return vo;
        }
        //判断用户是否实名
        if (user.getIdCard() == null) {
            vo.setCode(RechargeEnum.RECHARGE_USER_NO_REAL_NAME.getCode());
            vo.setMessage(RechargeEnum.RECHARGE_USER_NO_REAL_NAME.getMessage());
            return vo;
        }
        //判断支付方式是否允许
        if (!("alipay".equals(paymentMethod) || "wechat".equals(paymentMethod))) {
            vo.setCode(RechargeEnum.RECHARGE_PAYMENT_NOT_SUPPORT.getCode());
            vo.setMessage(RechargeEnum.RECHARGE_PAYMENT_NOT_SUPPORT.getMessage());
            return vo;
        }
        //判断充值金额是否大于0
        if (rechargeMoney <= 0) {
            vo.setCode(RechargeEnum.RECHARGE_MONEY_MUST_POSITIVE_NUMBER.getCode());
            vo.setMessage(RechargeEnum.RECHARGE_MONEY_MUST_POSITIVE_NUMBER.getMessage());
            return vo;
        }
        //创建支付订单
        RechargeRecord rechargeRecord = new RechargeRecord();
        //设置订单编号
        rechargeRecord.setRechargeNo(UUID.randomUUID().toString());
        //设置充值说明
        rechargeRecord.setRechargeDesc("账户充值");
        //设置充值金额
        rechargeRecord.setRechargeMoney(rechargeMoney);
        //设置充值状态未待支付
        rechargeRecord.setRechargeStatus("0");
        //设置用户ID
        rechargeRecord.setUid(user.getId());
        //设置订单生成时间
        rechargeRecord.setRechargeTime(new Date());
        //调用支付接口
        String recharge = rechargeService.recharge(paymentMethod, rechargeRecord);
        if (!"".equals(recharge)){
            vo.setCode(RechargeEnum.RECHARGE_CREATE_ORDER_SUCCESS.getCode());
            vo.setMessage(RechargeEnum.RECHARGE_CREATE_ORDER_SUCCESS.getMessage());
            vo.setData(recharge);
        }
        return vo;
    }


}
