package com.pintor.dev.jigeumiyag.global.common.dto;

public record ApiResponse<T>(
        boolean success,
        T data,
        ErrorResponse error
) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>(true, null, null);
    }

    public static ApiResponse<ErrorResponse> fail(ErrorResponse error) {
        return new ApiResponse<>(false, null, error);
    }
}