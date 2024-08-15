package com.ohmea.todayrecipe.dto.recipe;

import com.ohmea.todayrecipe.entity.RecipeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseDTO {
    private Long id;
    private String name;
    private String link;
    private String imageUrl;
    private List<String> ingredients;
    private String recipe;
    private String serving;
    private String time;
    private String difficulty;
    private String keyword;
    private String category;
    private int manCount;
    private int womanCount;
    private int totalCount;
    private Integer oneBudget;
    private Integer totalBudget;

    public static RecipeResponseDTO toDto(RecipeEntity recipeEntity) {
        return RecipeResponseDTO.builder()
                .id(recipeEntity.getId())
                .name(recipeEntity.getName())
                .link(recipeEntity.getLink())
                .imageUrl(recipeEntity.getImgURL())
                .ingredients(RecipeIngredientDTO.toStringList(recipeEntity.getIngredients()))
                .recipe(recipeEntity.getRecipe())
                .serving(recipeEntity.getServing())
                .time(recipeEntity.getTime())
                .difficulty(recipeEntity.getDifficulty())
                .keyword(recipeEntity.getKeyword())
                .category(recipeEntity.getCategory())
                .manCount(recipeEntity.getManCount())
                .womanCount(recipeEntity.getWomanCount())
                .totalCount(recipeEntity.getTotalCount())
                .oneBudget(recipeEntity.getOneBudget())
                .totalBudget(recipeEntity.getTotalBudget())
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class list {
        private Long id;
        private String name;
        private String link;
        private String imageUrl;
        private String time;
        private String difficulty;
        private String keyword;
        private String category;
        private Integer oneBudget;
        private Integer totalBudget;
        private int manCount;
        private int womanCount;
        private int totalCount;

        public static list toDto(RecipeEntity recipeEntity) {
            return list.builder()
                    .id(recipeEntity.getId())
                    .name(recipeEntity.getName())
                    .link(recipeEntity.getLink())
                    .imageUrl(recipeEntity.getImgURL())
                    .time(recipeEntity.getTime())
                    .difficulty(recipeEntity.getDifficulty())
                    .keyword(recipeEntity.getKeyword())
                    .category(recipeEntity.getCategory())
                    .oneBudget(recipeEntity.getOneBudget())
                    .totalBudget(recipeEntity.getTotalBudget())
                    .manCount(recipeEntity.getManCount())
                    .womanCount(recipeEntity.getWomanCount())
                    .totalCount(recipeEntity.getTotalCount())
                    .build();
        }
    }
}
