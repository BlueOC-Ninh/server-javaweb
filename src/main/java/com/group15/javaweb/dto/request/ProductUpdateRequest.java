package com.group15.javaweb.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductUpdateRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm tối đa 255 ký tự")
    private String name;

    private MultipartFile avatarUrl;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @Min(value = 0, message = "Số lượng không được âm")
    private int quantity;

    @Min(value = 0, message = "Số lượng đã bán không được âm")
    private int soldCount;

    private String description;

    @NotBlank(message = "Danh mục sản phẩm không được để trống")
    private String category_id;

    private String receivingProcess;

    private String warrantyPolicy;

    private String frequentlyAskedQuestions;

    @DecimalMin(value = "0.0", message = "Giảm giá không được âm")
    @DecimalMax(value = "100.0", message = "Giảm giá tối đa là 100%")
    private BigDecimal discount;

    @Min(value = 0, message = "Số ngày hiệu lực không được âm")
    private int daysValid;
}
