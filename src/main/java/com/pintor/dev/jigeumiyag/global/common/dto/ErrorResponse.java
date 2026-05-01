package com.pintor.dev.jigeumiyag.global.common.dto;

import java.util.Map;

public record ErrorResponse(
        int status,
        String code,
        String message,
        Map<String, String> details
) {

    public static ErrorResponse of(int status, String code, String message) {
        return new ErrorResponse(status, code, message, null);
    }

    public static ErrorResponse of(int status, String code, String message, Map<String, String> details) {
        return new ErrorResponse(status, code, message, details);
    }
}