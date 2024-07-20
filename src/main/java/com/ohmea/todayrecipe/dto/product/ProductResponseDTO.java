package com.ohmea.todayrecipe.dto.product;

import com.ohmea.todayrecipe.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private int id;
    private String image;
    private String title;
    private String description;
    private String percent;
    private String price;
    private String url;

    public static ProductResponseDTO toDto(ProductEntity productEntity) {
        return ProductResponseDTO.builder()
                .id(productEntity.getId())
                .image(productEntity.getImage())
                .description(productEntity.getDescription())
                .percent(productEntity.getPercent())
                .price(productEntity.getPrice())
                .url(productEntity.getUrl())
                .build();
    }
}
