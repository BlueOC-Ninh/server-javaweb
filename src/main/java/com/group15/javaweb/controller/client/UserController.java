package com.group15.javaweb.controller.client;

import com.group15.javaweb.dto.response.ApiResponse;
import com.group15.javaweb.dto.UserCreationRequest;
import com.group15.javaweb.dto.UpdateMeRequest;
import com.group15.javaweb.dto.response.client.user.UserResponse;
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

    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable("userId") String userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    UserResponse updateUserById(@PathVariable("userId") String userId, @Valid @RequestBody UpdateMeRequest request){
    return userService.updateUserById(userId, request);
    }
}
