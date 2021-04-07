package cn.syned.p2p.controller;

import cn.syned.p2p.entity.FinaceAccount;
import cn.syned.p2p.entity.ResultVo;
import cn.syned.p2p.entity.User;
import cn.syned.p2p.enums.FinaceAccountEnum;
import cn.syned.p2p.service.FinaceAccountService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FinaceAccountController {
    @DubboReference(interfaceClass = FinaceAccountService.class, version = "1.0.0", timeout = 20000)
    FinaceAccountService finaceAccountService;

    @RequestMapping("/loan/page/queryAccount")
    @ResponseBody
    public Object queryAccount(HttpServletRequest request) {
        ResultVo vo = new ResultVo();
        User user = (User) request.getSession().getAttribute("user");
        if (!ObjectUtils.allNotNull(user)) {
            vo.setCode(FinaceAccountEnum.FINACEACCOUNT_USER_NO_LOGIN_ERROR.getCode());
            vo.setMessage(FinaceAccountEnum.FINACEACCOUNT_USER_NO_LOGIN_ERROR.getMessage());
            return vo;
        }
        if (user.getIdCard() == null || "".equals(user.getIdCard())) {
            vo.setCode(FinaceAccountEnum.FINACEACCOUNT_REAL_NAME_FAIL.getCode());
            vo.setMessage(FinaceAccountEnum.FINACEACCOUNT_REAL_NAME_FAIL.getMessage());
            return vo;
        }
        //根据用户编号查询账户信息
        FinaceAccount finaceAccount = finaceAccountService.queryFinaceAccountByUserId(user.getId());
        if (finaceAccount != null) {
            //账户放入session中
            request.getSession().setAttribute("finaceAccount", finaceAccount);
            vo.setCode(FinaceAccountEnum.FINACEACCOUNT_QUERY_SUCCESS.getCode());
            vo.setMessage(FinaceAccountEnum.FINACEACCOUNT_QUERY_SUCCESS.getMessage());
            vo.setData(finaceAccount.getAvailableMoney());
        } else {
            vo.setCode(FinaceAccountEnum.FINACEACCOUNT_QUERY_SUCCESS.getCode());
            vo.setMessage(FinaceAccountEnum.FINACEACCOUNT_QUERY_SUCCESS.getMessage());
        }
        return vo;
    }
}
