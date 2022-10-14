package com.study.server.core.exception;

import com.study.server.core.model.ApiResponse;
import com.study.server.core.model.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorMessage>> businessExceptionHandler(HttpServletRequest request, BusinessException exception) {
        printLog(request, exception);
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ApiResponse<ErrorMessage>> responseStatusExceptionHandler(HttpServletRequest request, ResponseStatusException exception) {
        if (log.isDebugEnabled()) {
            printLog(request, exception);
        }
        return createResponseEntity(exception.getStatus(), new ErrorMessage(exception.getReason()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<ErrorMessage>> exceptionHandler(HttpServletRequest request, Exception exception) {
        printLog(request, exception);
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorMessage(exception.getMessage()));
    }

    private ResponseEntity<ApiResponse<ErrorMessage>> createResponseEntity(HttpStatus httpStatus, ErrorMessage data) {
        return ResponseEntity
                .status(httpStatus)
                .body(new ApiResponse<>(data));
    }

    private void printLog(HttpServletRequest request, Exception exception) {
        log.error("{} {}", request.getMethod(), request.getRequestURI(), exception);
    }
}
