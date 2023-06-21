package com.vivid.dream.vo.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ExceptionDetailResponseDTO {
    String time;
    int status;
    String method;
    String path;
    String error;
    String message;
}
