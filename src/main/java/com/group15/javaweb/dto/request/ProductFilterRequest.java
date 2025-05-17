package com.group15.javaweb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterRequest {
    private String name;
    private String categoryId;
    private String sortBy = "createdAt";
    private String sortDir = "desc";
    private Boolean deleted;
    private int page = 0;
    private int size = 10;
}
