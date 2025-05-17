package com.group15.javaweb.dto.request;

import com.group15.javaweb.dto.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {

    @NotBlank(message = "Vui lòng điền tên người dùng")
    @Size(min = 3, message = "Tên người dùng phải có tối thiểu 3 kí tự")
    private String username;

    @NotBlank(message = "Vui lòng điền email")
    @Email(message = "Vui lòng điền đúng định dạng email")
    private String email;

    @NotBlank(message = "Vui lòng điền mật khẩu")
    @Size(min = 8, message = "Mật khẩu phải có tối thiểu 8 kí tự")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "Mật khẩu phải có ít nhất 1 chữ cái, 1 số và 1 kí tự đặc biệt"
    )
    private String password;



    @Pattern(
            regexp = "^0[0-9]{9}$",
            message = "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có tổng cộng 10 chữ số"
    )
    private String phone;

    private Role role = Role.USER;

    private boolean isActive = true;
}
