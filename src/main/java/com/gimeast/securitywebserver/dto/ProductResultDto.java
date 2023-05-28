package com.gimeast.securitywebserver.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductResultDto {
    private Long number;
    private String name;
    private Integer price;
    private Integer stock;
}
