package com.group15.javaweb.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank(message = "Vui lòng điền email")
    @Email(message = "Vui lòng điền đúng định dạng email")
     String email;

    @NotBlank(message = "Vui lòng điền mật khẩu")
    @Size(min = 8, message = "Mật khẩu phải có tối thiểu 8 kí tự")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "Mật khẩu phải có ít nhất 1 chữ cái, 1 số và 1 kí tự đặc biệt"
    )
     String password;
}
