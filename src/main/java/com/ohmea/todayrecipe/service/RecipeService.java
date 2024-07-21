package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.recipe.RecipeResponseDTO;
import com.ohmea.todayrecipe.entity.RecipeEntity;
import com.ohmea.todayrecipe.repository.RecipeRepository;
import com.ohmea.todayrecipe.util.CsvReader;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CsvReader csvReader;
    private static final String CSV_FILE_PATH = "static/recipe_entity.csv";

    @PostConstruct
    public void init() {
        List<RecipeEntity> recipes = csvReader.readRecipesFromCsv(CSV_FILE_PATH);
        recipeRepository.saveAll(recipes);
    }

    public List<RecipeResponseDTO> getAllRecipes() {
        List<RecipeEntity> recipeEntities = recipeRepository.findAll();
        List<RecipeResponseDTO> recipeResponseDTOList = new ArrayList<>();

        recipeEntities.forEach(entity -> {
            recipeResponseDTOList.add(RecipeResponseDTO.toDto(entity));
        });

        return recipeResponseDTOList;
    }

    public List<RecipeResponseDTO> searchRecipesByName(String name) {
        List<RecipeEntity> recipeEntities = recipeRepository.findByNameContainingIgnoreCase(name);
        List<RecipeResponseDTO> recipeResponseDTOList = new ArrayList<>();

        recipeEntities.forEach(entity -> {
            recipeResponseDTOList.add(RecipeResponseDTO.toDto(entity));
        });

        return recipeResponseDTOList;
    }

    public List<RecipeResponseDTO> findRelatedRecipesByIngredients(String name) {
        List<RecipeEntity> allRecipes = recipeRepository.findAll();
        List<RecipeResponseDTO> relatedRecipes = new ArrayList<>();

        RecipeEntity targetRecipe = allRecipes.stream()
                .filter(recipe -> recipe.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (targetRecipe != null) {
            String targetIngredients = targetRecipe.getIngredients(); // 기준 레시피 재료

            // 다른 레시피와 비교
            for (RecipeEntity recipe : allRecipes) {
                if (!recipe.getName().equalsIgnoreCase(name)) {
                    String otherIngredients = recipe.getIngredients(); // 비교할 레시피 재료

                    // 겹치는 재료 수 계산
                    int overlapCount = countOverlappingIngredients(targetIngredients, otherIngredients);

                    // 겹치는 재료가 10개 이상인지 확인
                    if (overlapCount >= 10) {
                        relatedRecipes.add(RecipeResponseDTO.toDto(recipe));
                    }
                }
            }
        }

        return relatedRecipes;
    }


    // 두 문자열에서 겹치는 글자 수 카운트
    private int countOverlappingIngredients(String str1, String str2) {
        // 쉼표, 공백 제거
        String[] ingredients1 = str1.replace(",", "").split("\\s+");
        String[] ingredients2 = str2.replace(",", "").split("\\s+");

        Set<String> uniqueIngredients1 = new HashSet<>(Arrays.asList(ingredients1));
        Set<String> uniqueIngredients2 = new HashSet<>(Arrays.asList(ingredients2));

        // 중복된 재료 수 카운트
        uniqueIngredients1.retainAll(uniqueIngredients2);

        return uniqueIngredients1.size();
    }



}
