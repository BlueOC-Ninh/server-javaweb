package com.group15.javaweb.dto.response;

import com.group15.javaweb.dto.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String email;
    Role role;
    String accessToken;
    String user_id;
}
