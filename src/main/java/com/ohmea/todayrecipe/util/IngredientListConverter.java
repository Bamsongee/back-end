package com.ohmea.todayrecipe.util;

import com.ohmea.todayrecipe.entity.RecipeIngredientEntity;
import com.opencsv.bean.AbstractBeanField;

import java.util.ArrayList;
import java.util.List;

public class IngredientListConverter extends AbstractBeanField<List<RecipeIngredientEntity>, String> {

    @Override
    protected List<RecipeIngredientEntity> convert(String value) {
        List<RecipeIngredientEntity> ingredients = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            String[] ingredientArray = value.split(",");
            for (String ingredientName : ingredientArray) {
                ingredients.add(new RecipeIngredientEntity(null, ingredientName.trim()));
            }
        }
        return ingredients;
    }
}
