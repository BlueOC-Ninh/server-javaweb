package com.group15.javaweb.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMeRequest {
    @Size(min = 3, message = "Tên người dùng phải có tối thiểu 3 kí tự")
    private String username;

    @Pattern(
            regexp = "^0[0-9]{9}$",
            message = "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có tổng cộng 10 chữ số"
    )
    private String phone;

    @NotBlank(message = "Vui lòng điền mật khẩu")
    @Size(min = 8, message = "Mật khẩu phải có tối thiểu 8 kí tự")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "Mật khẩu phải có ít nhất 1 số và 1 kí tự đặc biệt"
    )
    private String password;

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
