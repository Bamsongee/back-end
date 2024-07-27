package com.ohmea.todayrecipe.dto.ingredient;

import com.ohmea.todayrecipe.entity.IngredientEntity;
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
public class IngredientResponseDTO {
    private Long id;
    private String ingredients;
    private Integer count;

    public static IngredientResponseDTO toDto(IngredientEntity entity) {
        return IngredientResponseDTO.builder()
                .id(entity.getId())
                .ingredients(entity.getIngredient())
                .count(entity.getCount())
                .build();
    }
}
