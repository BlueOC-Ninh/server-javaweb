package com.group15.javaweb.service;


import com.group15.javaweb.dto.Role;
import com.group15.javaweb.dto.request.LoginRequest;
import com.group15.javaweb.dto.request.RegisterRequest;
import com.group15.javaweb.dto.response.LoginResponse;
import com.group15.javaweb.dto.response.UserResponse;
import com.group15.javaweb.entity.User;
import com.group15.javaweb.exception.ApiException;
import com.group15.javaweb.mapper.UserMapper;
import com.group15.javaweb.repository.UserRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    private final String SECRET;

    @Autowired
    public AuthService(@Value("${jwt.privateKey}") String SECRET) {
        this.SECRET = SECRET;
    }

    public LoginResponse login(LoginRequest request) throws Exception {
       User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new ApiException(400, "Tài khoản hoặc mật khẩu không chính xác"));

       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
       String token = createJWT(user.getId(), user.getRole());
       if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
           return new LoginResponse(user.getEmail(), user.getRole(), token, user.getId());
       } else {
           throw new ApiException(400, "Tài khoản hoặc mật khẩu không chính xác");
       }
   }

   public UserResponse signup(RegisterRequest request){
       User user = userMapper.toUser(request);
       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
       user.setPassword(passwordEncoder.encode(request.getPassword()));
       user.setRole(Role.USER);
       if(userRepository.existsByEmail(request.getEmail()))
           throw  new  ApiException(400, "Email đã tồn tại");
       return userMapper.toUserResponse(userRepository.save(user));
   }


    public  String createJWT(String  id, Role role) throws Exception {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("Group-15")
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7))
                .claim("id", id)
                .claim("role", role)
                .build();

        JWSSigner signer = new MACSigner(SECRET);
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }
}
