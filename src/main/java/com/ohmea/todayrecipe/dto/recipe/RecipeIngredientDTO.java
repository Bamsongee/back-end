package com.ohmea.todayrecipe.dto.recipe;

import com.ohmea.todayrecipe.entity.RecipeIngredientEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientDTO {
    private List<String> ingredients;

    public static  RecipeIngredientDTO toDTO(List<RecipeIngredientEntity> entities) {
        List<String> ingredients =  entities.stream()
                .map(RecipeIngredientEntity::getIngredient).toList();

        return RecipeIngredientDTO.builder()
                .ingredients(ingredients)
                .build();
    }
}
