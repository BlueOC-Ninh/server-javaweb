package com.group15.javaweb.controller;

import com.group15.javaweb.dto.response.ApiResponse;
import com.group15.javaweb.dto.request.UpdateMeRequest;
import com.group15.javaweb.dto.response.UserResponse;
import com.group15.javaweb.entity.User;
import com.group15.javaweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
     ApiResponse<List<User>> getAllUsers(){
        return ApiResponse.success("Lấy thông tin danh sách người dùng thành công",userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable("userId") String userId){
        return userService.getUserById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    UserResponse updateUserById(@PathVariable("userId") String userId, @Valid @RequestBody UpdateMeRequest request){
    return userService.updateUserById(userId, request);
    }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteProduct(@PathVariable String id) {
    userService.deleteUser(id);
    return ApiResponse.success("Xóa tài khoản thành công thành công",null);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}")
  public ApiResponse<Void> restoreProduct(@PathVariable String id) {
    userService.restoreUser(id);
    return ApiResponse.success("Khôi phục tài khoản thành công",null);
  }

}
