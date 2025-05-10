package com.group15.javaweb.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T> {
    private int code;
    private String message;
    private T result;

    public static <T> ApiResponse<T> createdSuccess(String message, T result) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setCode(201);
        res.setMessage(message);
        res.setResult(result);
        return res;
    }

    public static <T> ApiResponse<T> success(String message, T result) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setCode(200);
        res.setMessage(message);
        res.setResult(result);
        return res;
    }


}
