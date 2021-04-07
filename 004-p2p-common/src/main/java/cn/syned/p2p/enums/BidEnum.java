package cn.syned.p2p.enums;

public enum BidEnum {
    BID_INVEST_SUCCESS(200, "投资成功"),

    BID_USER_NO_LOGIN(400, "用户未登录"),
    BID_USER_NO_REAL_NAME(401, "用户未实名"),
    BID_MONEY_ILLEGAL(402, "投资金额不合法"),
    BID_MONEY_MUST_BE_MULTIPLE(403, "投资金额必须为100的倍数"),
    BID_MONEY_MIN_LIMIT(404, "不能小于最低投资金额"),
    BID_MONEY_MAX_LIMIT(405, "不能大于最高投资金额"),
    BID_FULL_MARK(406, "不能大于最高投资金额"),
    BID_MONEY_EXCEEDING_THE_REMAINING(407, "不能大于剩余可投金额"),
    BID_MONEY_INSUFFICIENT(408, "余额不足"),
    BID_UPDATE_LOAN_INFO_MONEY_FAIL(409, "更新产品剩余投标金额失败"),
    BID_UPDATE_FULL_MARK_FAIL(410, "更新产品满投标状态失败"),
    BID_INSERT_BID_INFO_FAIL(411, "添加交易记录失败"),
    BID_UNDEFINED_ERROR(999, "未知错误");

    private Integer code;
    private String message;

    BidEnum(Integer code, String message) {
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
