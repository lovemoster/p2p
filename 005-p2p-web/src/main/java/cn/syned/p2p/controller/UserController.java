package cn.syned.p2p.controller;

import cn.syned.p2p.entity.ResultVo;
import cn.syned.p2p.entity.User;
import cn.syned.p2p.enums.UserEnum;
import cn.syned.p2p.service.RedisService;
import cn.syned.p2p.service.UserService;
import cn.syned.p2p.utils.VerificationCodeUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @DubboReference(interfaceClass = UserService.class, version = "1.0.0", timeout = 20000)
    public UserService userService;

    @DubboReference(interfaceClass = RedisService.class, version = "1.0.0", timeout = 20000)
    public RedisService redisService;

    @GetMapping(path = "/loan/page/register")
    public String toRegister() {
        return "register";
    }

    @GetMapping(path = "/loan/page/login")
    public String toLogin() {
        return "login";
    }

    @GetMapping(path = "/loan/page/realName")
    public String toRealName() {
        return "realName";
    }

    @GetMapping("/loan/myCenter")
    public String toMyCenter(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "redirect:page/login";
        }
        return "myCenter";
    }

    /**
     * 用户手机号校验，校验是否为注册用户
     *
     * @param phone 用户手机号
     * @return 结果视图
     */
    @RequestMapping(path = "/loan/page/checkPhone")
    @ResponseBody
    public ResultVo checkPhone(@RequestParam(name = "phone") String phone) {
        Boolean isRegister = userService.checkPhone(phone);
        ResultVo vo = new ResultVo();
        if (isRegister) {
            vo.setCode(UserEnum.USER_REGISTER.getCode());
            vo.setMessage(UserEnum.USER_REGISTER.getMessage());
        } else {
            vo.setCode(UserEnum.USER_UNREGISTER.getCode());
            vo.setMessage(UserEnum.USER_UNREGISTER.getMessage());
        }
        return vo;
    }

    /**
     * 用户注册
     *
     * @param user    用户对象
     * @param code    验证码
     * @param session 会话
     * @return 结果视图
     */
    @PostMapping(path = "/loan/page/register")
    @ResponseBody
    public ResultVo register(User user, @RequestParam("code") String code, HttpSession session) {
        ResultVo vo = new ResultVo();
        //判断验证码是否一致
        String resCode = redisService.popCode(user.getPhone());
        //判断验证码是否为空
        if (resCode == null || "".equals(resCode)) {
            vo.setCode(UserEnum.USER_CODE_NOT_EMPTY.getCode());
            vo.setMessage(UserEnum.USER_CODE_NOT_EMPTY.getMessage());
            return vo;
        }
        if (code == null || "".equals(code)) {
            vo.setCode(UserEnum.USER_CODE_NOT_EMPTY.getCode());
            vo.setMessage(UserEnum.USER_CODE_NOT_EMPTY.getMessage());
            return vo;
        }
        //判断验证码是否一致
        if (!resCode.equals(code)) {
            vo.setCode(UserEnum.USER_CODE_ATYPISM.getCode());
            vo.setMessage(UserEnum.USER_CODE_ATYPISM.getMessage());
            return vo;
        }
        Boolean status = userService.register(user);
        if (status) {
            vo.setCode(UserEnum.USER_REGISTER_SUCCESS.getCode());
            vo.setMessage(UserEnum.USER_REGISTER_SUCCESS.getMessage());
            session.setAttribute("user", user);
        } else {
            vo.setCode(UserEnum.USER_REGISTER_FAIL.getCode());
            vo.setMessage(UserEnum.USER_REGISTER_FAIL.getMessage());
        }
        return vo;
    }

    /**
     * 发送验证码
     *
     * @param phone   手机号码
     * @param session 会话
     * @return 结果视图
     */
    @PostMapping(path = "/loan/page/sendCode")
    @ResponseBody
    public ResultVo sendCode(@RequestParam(name = "phone", required = false) String phone, HttpSession session) {
        //判断是否传入手机号码，如果不存在则从session中获取
        if (phone == null || "".equals(phone)) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                phone = user.getPhone();
            }
        }
        ResultVo vo = new ResultVo();
        //生成验证码
        String verificationCode = VerificationCodeUtil.getVerificationCode(6);
        //设置短信接口地址
        String url = "https://way.jd.com/kaixintong/kaixintong";
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("appkey", "c5afd4c4272f7e2ca6961bb209c95a40");
        parameter.put("mobile", phone);
        parameter.put("content", "【凯信通】您的验证码是：" + verificationCode);
        //模拟报文
        String result = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 0,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-7225293</remainpoint>\\n <taskID>171845880</taskID>\\n <successCounts>1</successCounts></returnsms>\",\n" +
                "    \"requestId\": \"f9f2178421aa4eb5a1f124e42f3ccc69\"\n" +
                "}";

        try {
            //发送验证码
            //result = HttpClientUtils.doPost(url, parameter);

            //解析响应信息
            JSONObject jsonObject = JSONObject.parseObject(result);
            //获得通信信息
            String code1 = jsonObject.getString("code");
            if (!StringUtils.equals("10000", code1)) {
                vo.setCode(UserEnum.USER_SEND_CODE_FAIL.getCode());
                vo.setMessage(UserEnum.USER_SEND_CODE_FAIL.getMessage());
                return vo;
            }

            //获取xml
            String xml = jsonObject.getString("result");

            //xml解析
            Document document = DocumentHelper.parseText(xml);
            Node node = document.selectSingleNode("//returnstatus");
            String text = node.getText();
            if (!StringUtils.equals("Success", text)) {
                vo.setCode(UserEnum.USER_SEND_CODE_FAIL.getCode());
                vo.setMessage(UserEnum.USER_SEND_CODE_FAIL.getMessage());
                return vo;
            }
            //将验证码放入Redis中
            redisService.pushCode(phone, verificationCode);
            vo.setCode(UserEnum.USER_SEND_CODE_SUCCESS.getCode());
            vo.setMessage(UserEnum.USER_SEND_CODE_SUCCESS.getMessage());
            vo.setData(verificationCode);
        } catch (Exception e) {
            e.printStackTrace();
            vo.setCode(UserEnum.USER_SEND_CODE_FAIL.getCode());
            vo.setMessage(UserEnum.USER_SEND_CODE_FAIL.getMessage());
            return vo;
        }
        return vo;
    }

    /**
     * 用户实名方法
     *
     * @param user    用户对象
     * @param code    验证码
     * @param session 会话
     * @return 结果视图
     */
    @PostMapping(path = "/loan/page/realName")
    @ResponseBody
    public ResultVo realName(User user, @RequestParam("code") String code, HttpSession session) {
        ResultVo vo = new ResultVo();
        User realUser = (User) session.getAttribute("user");
        user.setPhone(realUser.getPhone());
        //判断验证码是否一致
        String resCode = redisService.popCode(realUser.getPhone());
        //判断验证码是否为空
        if (resCode == null || "".equals(resCode)) {
            vo.setCode(UserEnum.USER_CODE_NOT_EMPTY.getCode());
            vo.setMessage(UserEnum.USER_CODE_NOT_EMPTY.getMessage());
            return vo;
        }
        if (code == null || "".equals(code)) {
            vo.setCode(UserEnum.USER_CODE_NOT_EMPTY.getCode());
            vo.setMessage(UserEnum.USER_CODE_NOT_EMPTY.getMessage());
            return vo;
        }
        //判断验证码是否一致
        if (!resCode.equals(code)) {
            vo.setCode(UserEnum.USER_CODE_ATYPISM.getCode());
            vo.setMessage(UserEnum.USER_CODE_ATYPISM.getMessage());
            return vo;
        }

        //身份验证接口
        String url = "https://way.jd.com/youhuoBeijing/test";

        Map<String, Object> parasMap = new HashMap<>();
        parasMap.put("realName", user.getName());
        parasMap.put("cardNo", user.getIdCard());
        parasMap.put("appkey", "c5afd4c4272f7e2ca6961bb209c95a40");

        String result = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"error_code\": 0,\n" +
                "        \"reason\": \"成功\",\n" +
                "        \"result\": {\n" +
                "            \"realname\": \"乐天磊\",\n" +
                "            \"idcard\": \"350721197702134399\",\n" +
                "            \"isok\": true\n" +
                "        }\n" +
                "    }\n" +
                "}";

        //调用远端接口进行身份验证
        try {
            // result = HttpClientUtils.doPost(url, parasMap);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String code1 = jsonObject.getString("code");
            if (!StringUtils.equals("10000", code1)) {
                vo.setCode(UserEnum.USER_REMOTE_AUTHENTICATION_INTERFACE_NOT_RESPONDING.getCode());
                vo.setMessage(UserEnum.USER_REMOTE_AUTHENTICATION_INTERFACE_NOT_RESPONDING.getMessage());
                return vo;
            }
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
            if (!isok) {
                vo.setCode(UserEnum.USER_REMOTE_AUTHENTICATION_INCONSISTENT_INFORMATION.getCode());
                vo.setMessage(UserEnum.USER_REMOTE_AUTHENTICATION_INCONSISTENT_INFORMATION.getMessage());
                return vo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            vo.setCode(UserEnum.USER_REMOTE_AUTHENTICATION_INTERFACE_NOT_RESPONDING.getCode());
            vo.setMessage(UserEnum.USER_REMOTE_AUTHENTICATION_INTERFACE_NOT_RESPONDING.getMessage());
            return vo;
        }
        Boolean status = userService.realName(user);
        if (status) {
            vo.setCode(UserEnum.USER_REAL_NAME_SUCCESS.getCode());
            vo.setMessage(UserEnum.USER_REAL_NAME_SUCCESS.getMessage());
            logout(session);
        } else {
            vo.setCode(UserEnum.USER_REAL_NAME_FAIL.getCode());
            vo.setMessage(UserEnum.USER_REAL_NAME_FAIL.getMessage());
        }
        return vo;
    }

    /**
     * 用户登录方法
     *
     * @param user    待验证的用户
     * @param session 会话
     * @return 结果视图
     */
    @PostMapping(path = "/loan/page/login")
    @ResponseBody
    public ResultVo login(User user, HttpSession session) {
        ResultVo vo = new ResultVo();
        //验证手机号是否为空
        if ("".equals(user.getPhone()) || user.getPhone() == null) {
            vo.setCode(UserEnum.USER_LOGIN_PHONE_EMPTY.getCode());
            vo.setMessage(UserEnum.USER_LOGIN_PHONE_EMPTY.getMessage());
            return vo;
        }
        //验证密码是否为空
        if ("".equals(user.getLoginPassword()) || user.getLoginPassword() == null) {
            vo.setCode(UserEnum.USER_LOGIN_LOGIN_PASSWORD_EMPTY.getCode());
            vo.setMessage(UserEnum.USER_LOGIN_LOGIN_PASSWORD_EMPTY.getMessage());
            return vo;
        }
        //查询数据库，校验用户名密码
        User resUser = userService.login(user);
        if (resUser != null) {
            session.setAttribute("user", resUser);
            vo.setCode(UserEnum.USER_LOGIN_SUCCESS.getCode());
            vo.setMessage(UserEnum.USER_LOGIN_SUCCESS.getMessage());
        } else {
            vo.setCode(UserEnum.USER_LOGIN_PHONE_OR_PASSWORD_ERROR.getCode());
            vo.setMessage(UserEnum.USER_LOGIN_PHONE_OR_PASSWORD_ERROR.getMessage());
        }
        return vo;
    }

    @GetMapping(path = "/loan/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:../index";
    }
}