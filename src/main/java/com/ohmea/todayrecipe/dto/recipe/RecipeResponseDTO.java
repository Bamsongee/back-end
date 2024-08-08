package com.ohmea.todayrecipe.dto.recipe;

import com.ohmea.todayrecipe.entity.RecipeEntity;
import jakarta.persistence.Column;
import com.ohmea.todayrecipe.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseDTO {
    private String ranking;
    private String name;
    private String link;
    private String imageUrl;
    private String ingredients;
    private String recipe;
    private String serving;
    private String time;
    private String difficulty;

    public static RecipeResponseDTO toDto(RecipeEntity recipeEntity) {
        return RecipeResponseDTO.builder()
                .ranking(recipeEntity.getRanking())
                .name(recipeEntity.getName())
                .link(recipeEntity.getLink())
                .imageUrl(recipeEntity.getImgURL())
                .ingredients(recipeEntity.getIngredients())
                .recipe(recipeEntity.getRecipe())
                .serving(recipeEntity.getServing())
                .time(recipeEntity.getTime())
                .difficulty(recipeEntity.getDifficulty())
                .build();
    }
}
