package com.group15.javaweb.controller;

import com.group15.javaweb.dto.request.ProductFilterRequest;
import com.group15.javaweb.dto.request.ProductCreateRequest;
import com.group15.javaweb.dto.request.ProductUpdateRequest;
import com.group15.javaweb.dto.response.ApiResponse;
import com.group15.javaweb.entity.Product;
import com.group15.javaweb.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public ApiResponse<Product> createProduct(@Valid @ModelAttribute ProductCreateRequest request) {
        try {
            Product savedProduct = productService.createProduct(request);
            return ApiResponse.createdSuccess("Thêm mới sản phẩm thành công",savedProduct);
        } catch (IOException e) {
            throw new MultipartException("Lỗi tải ảnh lên", e);
        }
    }
    @GetMapping("/{id}")
    public ApiResponse<Product> getProductDetail(@PathVariable String id) {
        Product product = productService.getProductDetail(id);
        return ApiResponse.success("Lấy chi tiết sản phẩm thành công", product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ApiResponse<Product> updateProduct(
            @PathVariable("id") String productId,
            @Valid @ModelAttribute ProductUpdateRequest request
    ) {
        try {
            Product updatedProduct = productService.updateProduct(productId, request);
            return ApiResponse.success("Cập nhật sản phẩm thành công", updatedProduct);
        } catch (IOException e) {
            throw new MultipartException("Lỗi tải ảnh lên", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ApiResponse.success("Xóa sản phẩm thành công",null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ApiResponse<Void> restoreProduct(@PathVariable String id) {
        productService.restoreProduct(id);
        return ApiResponse.success("Khôi phục sản phẩm thành công",null);
    }

    @GetMapping
    public Page<Product> getProducts(ProductFilterRequest filter) {
        return productService.getFilteredProducts(filter);
    }
}
