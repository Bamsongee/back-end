package com.ohmea.todayrecipe.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngredientDTO {
    private String ingredient;
    private Integer count;
}
