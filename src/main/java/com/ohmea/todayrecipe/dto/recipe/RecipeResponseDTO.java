package com.ohmea.todayrecipe.dto.recipe;

import com.ohmea.todayrecipe.entity.RecipeEntity;
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
    private String keyword;
    private String category;
    private int manCount;
    private int womanCount;
    private int totalCount;

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
                .keyword(recipeEntity.getKeyword())
                .category(recipeEntity.getCategory())
                .manCount(recipeEntity.getManCount())
                .womanCount(recipeEntity.getWomanCount())
                .totalCount(recipeEntity.getTotalCount())
                .build();
    }
}
