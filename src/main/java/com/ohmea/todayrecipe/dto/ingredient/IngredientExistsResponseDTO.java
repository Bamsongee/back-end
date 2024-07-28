package com.ohmea.todayrecipe.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientExistsResponseDTO {
    private String ingredients;
    private Boolean isExists; // 기존에 존재하여 save되지 않은 경우 false, 기존에 없어 save된 경우 true
}
