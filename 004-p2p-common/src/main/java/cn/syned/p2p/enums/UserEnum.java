package cn.syned.p2p.enums;

public enum UserEnum {
    USER_UNREGISTER(200, "用户未注册"),
    USER_REGISTER_SUCCESS(201, "用户注册成功"),
    USER_SEND_CODE_SUCCESS(202, "验证码发送成功"),
    USER_REAL_NAME_SUCCESS(203, "用户实名成功"),
    USER_LOGIN_SUCCESS(204, "用户登录成功"),

    USER_REGISTER(400, "用户已注册"),
    USER_REGISTER_FAIL(401, "用户注册失败"),
    USER_SEND_CODE_FAIL(402, "验证码发送失败"),
    USER_CODE_NOT_EMPTY(403, "验证码不能为空"),
    USER_CODE_ATYPISM(404, "验证码错误"),
    USER_REAL_NAME_FAIL(405, "用户实名失败"),
    USER_REMOTE_AUTHENTICATION_INTERFACE_NOT_RESPONDING(406, "远端身份验证接口无响应"),
    USER_REMOTE_AUTHENTICATION_INCONSISTENT_INFORMATION(407, "身份信息不一致"),
    USER_LOGIN_PHONE_EMPTY(408, "账号为空"),
    USER_LOGIN_LOGIN_PASSWORD_EMPTY(409, "密码为空"),
    USER_LOGIN_PHONE_OR_PASSWORD_ERROR(410, "用户名或密码错误"),
    USER_UNDEFINED_ERROR(999, "未知错误");

    private Integer code;
    private String message;

    UserEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
