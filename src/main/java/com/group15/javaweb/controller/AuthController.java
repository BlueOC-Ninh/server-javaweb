package com.group15.javaweb.controller;


import com.group15.javaweb.dto.request.LoginRequest;
import com.group15.javaweb.dto.request.RegisterRequest;
import com.group15.javaweb.dto.response.ApiResponse;
import com.group15.javaweb.dto.response.LoginResponse;
import com.group15.javaweb.dto.response.UserResponse;
import com.group15.javaweb.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
     private AuthService authService;

    @PostMapping("/login")
    ApiResponse<LoginResponse> login (@Valid @RequestBody  LoginRequest request) throws Exception {
        return ApiResponse.success("Đăng nhập thành công", authService.login(request));
    }

    @PostMapping("/register")
    ApiResponse<UserResponse> register (@Valid @RequestBody RegisterRequest request){
        return ApiResponse.createdSuccess("Đăng kí tài khoản thành công", authService.signup(request));
    }
}
