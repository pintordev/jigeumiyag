package com.pintor.dev.jigeumiyag.global.exception.handler;

import com.pintor.dev.jigeumiyag.global.common.dto.ApiResponse;
import com.pintor.dev.jigeumiyag.global.common.dto.ErrorResponse;
import com.pintor.dev.jigeumiyag.global.exception.common.ApiException;
import com.pintor.dev.jigeumiyag.global.exception.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.stream.Collectors;

import static com.pintor.dev.jigeumiyag.global.exception.common.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleApiException(ApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[ApiException] {} status: {}, code: {}, message: {}",
                errorCode.name(), errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(ErrorResponse.of(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage())));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNoResourceFound(NoResourceFoundException e) {
        ErrorCode errorCode = COMMON_NOT_FOUND;
        log.warn("[NoResourceFound] status: {}, code: {}, message: {}",
                errorCode.getStatus().value(), errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(ErrorResponse.of(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage())));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        ErrorCode errorCode = COMMON_METHOD_NOT_ALLOWED;
        log.warn("[MethodNotSupported] status: {}, code: {}, message: {}",
                errorCode.getStatus().value(), errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(ErrorResponse.of(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage())));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMessageNotReadable(HttpMessageNotReadableException e) {
        ErrorCode errorCode = COMMON_MESSAGE_NOT_READABLE;
        log.warn("[MessageNotReadable] status: {}, code: {}, message: {}",
                errorCode.getStatus().value(), errorCode.getCode(), e.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorResponse.of(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage())));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = COMMON_INVALID_REQUEST;
        String detail = String.format("%s: %s 타입이어야 합니다.",
                e.getName(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");
        log.warn("[ArgumentTypeMismatch] status: {}, code: {}, detail: {}",
                errorCode.getStatus().value(), errorCode.getCode(), detail);
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(ErrorResponse.of(errorCode.getStatus().value(), errorCode.getCode(), detail)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = COMMON_INVALID_REQUEST;
        Map<String, String> details = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        field -> field.getDefaultMessage() != null ? field.getDefaultMessage() : "유효하지 않은 값입니다.",
                        (existing, replacement) -> existing
                ));
        log.warn("[ArgumentNotValid] status: {}, code: {}, details: {}",
                errorCode.getStatus().value(), errorCode.getCode(), details);
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(ErrorResponse.of(errorCode.getStatus().value(), errorCode.getCode(), "입력값이 유효하지 않습니다.", details)));
    }

    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbort(ClientAbortException e) {
        log.warn("[ClientAbort] message: {}", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleException(Exception e) {
        ErrorCode errorCode = COMMON_UNEXPECTED_ERROR;
        log.error("[UnexpectedException] cause: {}, message: {}",
                e.getClass().getSimpleName(),
                e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.fail(ErrorResponse.of(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage())));
    }
}