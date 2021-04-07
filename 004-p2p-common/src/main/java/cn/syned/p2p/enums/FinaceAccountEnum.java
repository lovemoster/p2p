package cn.syned.p2p.enums;

public enum FinaceAccountEnum {
    FINACEACCOUNT_QUERY_SUCCESS(200, "查询成功"),

    FINACEACCOUNT_USER_NO_LOGIN_ERROR(400, "用户未登录"),
    FINACEACCOUNT_QUERY_FAIL(401, "查询失败"),
    FINACEACCOUNT_REAL_NAME_FAIL(402, "请验证姓名"),
    FINACEACCOUNT_UNDEFINED_ERROR(999, "未知错误");

    private Integer code;
    private String message;

    FinaceAccountEnum(Integer code, String message) {
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
