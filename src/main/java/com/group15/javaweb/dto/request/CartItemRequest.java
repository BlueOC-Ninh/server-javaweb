package com.group15.javaweb.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    @NotBlank(message = "Vui lòng điền email")
    @Email(message = "Vui lòng điền đúng định dạng email")
    private String email;

    @NotBlank(message = "Vui lòng điền mật khẩu")
    @Size(min = 8, message = "Mật khẩu phải có tối thiểu 8 kí tự")
    private String password;

    @NotBlank
    private String userId;

    @NotBlank
    private String productId;
}
