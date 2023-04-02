package com.vivid.dream.config.handler.exception;

import com.vivid.dream.config.handler.ResultCode;

import java.text.MessageFormat;

public class WebException extends RuntimeException {
    ResultCode resultCode;

    public ResultCode getResultCode() {
        return resultCode;
    }

    public WebException(ResultCode resultCode) {
        super(resultCode.getDesc());
        this.resultCode = resultCode;
    }

    public WebException(ResultCode resultCode, Object... args) {
        super(resultCode.getDesc());
        String message = MessageFormat.format(resultCode.getDesc(), args);
        resultCode.setDesc(message);
        this.resultCode = resultCode;
    }
}