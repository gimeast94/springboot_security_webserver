package com.gimeast.securitywebserver.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private Integer price;
    private Integer stock;
}
