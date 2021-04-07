package cn.syned.p2p.exception;

import cn.syned.p2p.enums.BidEnum;

/**
 * 投资业务异常
 */
public class BidException extends Exception {
    private Integer code;
    private String message;

    public BidException() {
        super();
    }

    public BidException(BidEnum e) {
        super(e.getMessage());
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
