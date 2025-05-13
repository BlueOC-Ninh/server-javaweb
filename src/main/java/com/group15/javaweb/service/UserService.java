package com.group15.javaweb.service;

import com.group15.javaweb.dto.UpdateMeRequest;
import com.group15.javaweb.dto.UserCreationRequest;
import com.group15.javaweb.dto.response.client.user.UserResponse;
import com.group15.javaweb.entity.User;
import com.group15.javaweb.exception.ApiException;
import com.group15.javaweb.mapper.UserMapper;
import com.group15.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
//    @Autowired được dùng để tự động tiêm (inject) một dependency — ở đây là UserRepository — vào trong UserService. Mục đích là để bạn không cần tự tay khởi tạo đối tượng UserRepository, Spring sẽ làm việc đó cho bạn dựa trên cấu hình và container của nó.
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

//    public User createUser(UserCreationRequest request){
//
//        User user = userMapper.toUser(request);
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        if(userRepository.existsByEmail(request.getEmail()))
//            throw  new  ApiException(400, "Email đã tồn tại");
//
//        return  userRepository.save(user);
//    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public UserResponse getUserById(String id){
        return  userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()-> new ApiException(400, "Không tìm thấy người dùng")));
    }

   public UserResponse updateUserById(String userId, UpdateMeRequest request){
        User user = userRepository.findById(userId).orElseThrow(()-> new ApiException(400, "Không tìm thấy người dùng"));

        userMapper.updateUser(user,request);

        return userMapper.toUserResponse( userRepository.save(user));
    }
}
