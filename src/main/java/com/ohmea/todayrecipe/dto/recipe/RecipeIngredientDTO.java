package com.ohmea.todayrecipe.dto.recipe;

import com.ohmea.todayrecipe.entity.RecipeIngredientEntity;

import java.util.List;


public class RecipeIngredientDTO {

    public static  List<String> toStringList(List<RecipeIngredientEntity> entities) {
        return entities.stream()
                .map(RecipeIngredientEntity::getIngredient).toList();
    }
}
