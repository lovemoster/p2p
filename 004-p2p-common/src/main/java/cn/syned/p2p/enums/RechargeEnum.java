package cn.syned.p2p.enums;

public enum RechargeEnum {
    RECHARGE_CREATE_ORDER_SUCCESS(200, "订单创建成功"),

    RECHARGE_USER_NO_LOGIN(400, "用户未登录"),
    RECHARGE_USER_NO_REAL_NAME(401, "用户未实名"),
    RECHARGE_PAYMENT_NOT_SUPPORT(402, "支付方式不支持"),
    RECHARGE_MONEY_MUST_POSITIVE_NUMBER(403, "充值金额必须为正数"),
    RECHARGE_UNDEFINED_ERROR(999, "未知错误");

    private Integer code;
    private String message;

    RechargeEnum(Integer code, String message) {
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
