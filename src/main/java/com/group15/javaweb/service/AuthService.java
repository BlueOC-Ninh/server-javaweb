package com.group15.javaweb.service;


import com.group15.javaweb.dto.request.LoginRequest;
import com.group15.javaweb.dto.request.RegisterRequest;
import com.group15.javaweb.dto.response.LoginResponse;
import com.group15.javaweb.dto.response.client.user.UserResponse;
import com.group15.javaweb.entity.User;
import com.group15.javaweb.exception.ApiException;
import com.group15.javaweb.mapper.UserMapper;
import com.group15.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

   public LoginResponse login(LoginRequest request){
       User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new ApiException(400, "Tài khoản hoặc mật khẩu không chính xác"));

       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

       if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
           return new LoginResponse("abcd");
       } else {
           throw new ApiException(400, "Tài khoản hoặc mật khẩu không chính xác");
       }
   }

   public UserResponse signup(RegisterRequest request){
       User user = userMapper.toUser(request);
       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
       user.setPassword(passwordEncoder.encode(request.getPassword()));
       if(userRepository.existsByEmail(request.getEmail()))
           throw  new  ApiException(400, "Email đã tồn tại");
       return userMapper.toUserResponse(userRepository.save(user));
   }
}
