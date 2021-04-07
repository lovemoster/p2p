package cn.syned.p2p.entity;

import java.io.Serializable;

public class ResultVo implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    public ResultVo() {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
