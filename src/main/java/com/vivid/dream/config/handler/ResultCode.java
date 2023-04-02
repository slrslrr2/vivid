package com.vivid.dream.config.handler;

public enum ResultCode {
    OK(0, "Request Successfully"),
    ERROR(1, "Error"),
    ERR_REQUEST_PARAMETER(2, "Request Parameter Error"),
    BAD_REQUEST(4, "Bad Request"),
    TODAY_SONG_TOP100_IS_EXIST(1001,  "The song top 100 is already existed."),;

    private Integer code;
    private String desc;

    private ResultCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
