package com.vivid.dream.config.handler.exception;

import com.vivid.dream.config.handler.ResultCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

@Getter
public class WebException extends RuntimeException {
    ResultCode resultCode;
    HttpStatus httpStatus;

    public ResultCode getResultCode() {
        return resultCode;
    }

    public WebException(ResultCode resultCode, HttpStatus httpStatus) {
        super(resultCode.getDesc());
        this.resultCode = resultCode;
        this.httpStatus = httpStatus;
    }

    public WebException(ResultCode resultCode, Object... args) {
        super(resultCode.getDesc());
        String message = MessageFormat.format(resultCode.getDesc(), args);
        resultCode.setDesc(message);
        this.resultCode = resultCode;
    }
}