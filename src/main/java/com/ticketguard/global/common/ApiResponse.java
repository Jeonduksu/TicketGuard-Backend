package com.ticketguard.global.common;

// api 응답
public record ApiResponse<T>(
        boolean success,
        T data,
        Object error
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true,data,null);
    }

    public static <T> ApiResponse<T> error(Object error) {
        return new ApiResponse<>(false,null,error);
    }
}
