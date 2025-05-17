package com.group15.javaweb.service;

import com.group15.javaweb.dto.request.UpdateMeRequest;
import com.group15.javaweb.dto.response.UserResponse;
import com.group15.javaweb.entity.Product;
import com.group15.javaweb.entity.User;
import com.group15.javaweb.exception.ApiException;
import com.group15.javaweb.mapper.UserMapper;
import com.group15.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
//    @Autowired được dùng để tự động tiêm (inject) một dependency — ở đây là UserRepository — vào trong UserService. Mục đích là để bạn không cần tự tay khởi tạo đối tượng UserRepository, Spring sẽ làm việc đó cho bạn dựa trên cấu hình và container của nó.
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(404, "Tài khoản không tồn tại"));

        user.setActive(false);
        userRepository.save(user);
    }

    public void restoreUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(404, "Tài khoản không tồn tại"));

        user.setActive(true);
        userRepository.save(user);
    }


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
