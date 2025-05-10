package com.group15.javaweb.dto.response;

import com.group15.javaweb.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductCreationRequest {
        @NotBlank(message = "Tên sản phẩm không được để trống")
        private String name;

        @NotBlank(message = "Hình ảnh sản phẩm không được để trống")
        private String avatarUrl;

        @NotNull(message = "Giá sản phẩm không được để trống")
        @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
        @Digits(integer = 10, fraction = 2)
        private BigDecimal price;

        @Min(value = 0, message = "Số lượng không được âm")
        private int quantity = 1;

        private String desc;

        @NotNull(message = "Danh mục là bắt buộc")
        private Long categoryId;

        private String receivingProcess;

        private String warrantyPolicy;

        private String frequentlyAskedQuestions;

        @DecimalMin(value = "0.0", message = "Giảm giá không được âm")
        @Digits(integer = 10, fraction = 2)
        private BigDecimal discount;

        @Min(value = 0, message = "Thời hạn phải >= 0")
        private int daysValid;




}
