package com.pintor.dev.jigeumiyag.global.common.dto;

import com.pintor.dev.jigeumiyag.global.exception.common.ErrorCode;

import java.util.Map;

public record ErrorResponse(
        int status,
        String code,
        String message,
        Map<String, String> details
) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static ErrorResponse of(ErrorCode errorCode, Map<String, String> details) {
        return new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage(), details);
    }
}