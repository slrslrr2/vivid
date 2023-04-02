package com.vivid.dream.config.handler;

import com.vivid.dream.config.handler.exception.WebException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class AllRestExceptionHandler {
    @ExceptionHandler(WebException.class)
    public ResponseEntity<ExceptionDetailResponseDTO> handleException(WebException e, HttpServletRequest request, HttpServletRequest response) {
        log.error("### WebException:{} ###", e.getMessage());
        return getExceptionResponseEntity(e.getResultCode().getDesc(), request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ExceptionDetailResponseDTO> constraintViolationException(ConstraintViolationException e, HttpServletRequest request, HttpServletRequest response) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + " ");
        }
        String message = strBuilder.toString();

        return getExceptionResponseEntity(message, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDetailResponseDTO> handleOtherException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        String message = Optional.ofNullable(e.getMessage()).orElse("An unknown error has occurred.");
        return getExceptionResponseEntity(message, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static final String ERROR_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static ResponseEntity<ExceptionDetailResponseDTO> getExceptionResponseEntity(String message, HttpServletRequest request, HttpStatus httpStatus) {
        log.error(message);
        ExceptionDetailResponseDTO responseDTO = ExceptionDetailResponseDTO.builder()
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern(ERROR_DATE_FORMAT)))
                .status(httpStatus.value())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .error(httpStatus.getReasonPhrase())
                .message(message).build();

        return new ResponseEntity<>(responseDTO, httpStatus);
    }
}

