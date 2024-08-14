package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.recipe.RecipeResponseDTO;
import com.ohmea.todayrecipe.entity.*;
import com.ohmea.todayrecipe.repository.RecipeRepository;
import com.ohmea.todayrecipe.repository.UserRepository;
import com.ohmea.todayrecipe.util.CsvReader;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CsvReader csvReader;

    @Autowired
    private UserRepository userRepository;
    private static final String CSV_FILE_PATH = "static/recipe_entity.csv";

    @PostConstruct
    public void init() {
        List<RecipeEntity> recipes = csvReader.readRecipesFromCsv(CSV_FILE_PATH);
        recipeRepository.saveAll(recipes);
    }


    public List<RecipeResponseDTO.list> getAllRecipes() {
        List<RecipeEntity> recipeEntities = recipeRepository.findAll();
        List<RecipeResponseDTO.list> recipeResponseDTOList = new ArrayList<>();

        recipeEntities.forEach(entity -> {
            recipeResponseDTOList.add(RecipeResponseDTO.list.toDto(entity));
        });

        return recipeResponseDTOList;
    }

    public List<RecipeResponseDTO.list> searchRecipesByName(String name) {
        List<RecipeEntity> recipeEntities = recipeRepository.findByNameContainingIgnoreCase(name);
        List<RecipeResponseDTO.list> recipeResponseDTOList = new ArrayList<>();

        recipeEntities.forEach(entity -> {
            recipeResponseDTOList.add(RecipeResponseDTO.list.toDto(entity));
        });

        return recipeResponseDTOList;
    }

    /*
    public List<RecipeResponseDTO> findRelatedRecipesByIngredients(String name) {
        List<RecipeEntity> allRecipes = recipeRepository.findAll();
        List<RecipeResponseDTO> relatedRecipes = new ArrayList<>();

        // 타겟 레시피 찾기
        RecipeEntity targetRecipe = allRecipes.stream()
                .filter(recipe -> recipe.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (targetRecipe != null) {
            List<RecipeIngredientEntity> targetIngredients = targetRecipe.getIngredients(); // 기준 레시피 재료

            // 다른 레시피와 비교
            for (RecipeEntity recipe : allRecipes) {
                if (!recipe.getName().equalsIgnoreCase(name)) {
                    List<RecipeIngredientEntity> otherIngredients = recipe.getIngredients(); // 비교할 레시피 재료

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


    // 두 재료 리스트에서 겹치는 재료 수 카운트
    private int countOverlappingIngredients(List<RecipeIngredientEntity> ingredients1, List<RecipeIngredientEntity> ingredients2) {
        Set<String> uniqueIngredients1 = ingredients1.stream()
                .map(RecipeIngredientEntity::getIngredient)
                .collect(Collectors.toSet());

        Set<String> uniqueIngredients2 = ingredients2.stream()
                .map(RecipeIngredientEntity::getIngredient)
                .collect(Collectors.toSet());

        // 중복된 재료 수 카운트
        uniqueIngredients1.retainAll(uniqueIngredients2);
        System.out.println(uniqueIngredients1.size());

        return uniqueIngredients1.size();
    } */

    //레시피 세부 조회
    public RecipeResponseDTO getRecipeByRanking(String ranking, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        RecipeEntity recipeEntity = recipeRepository.findById(ranking).orElse(null);
        if (recipeEntity == null) {
            return null;
        }
        recipeEntity.incrementViewCountByGender(user.getGender());
        return RecipeResponseDTO.toDto(recipeEntity);
    }

    public List<RecipeResponseDTO.list> getPosibleRecipes(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        List<LikeEntity> likeEntities = user.getLikes();
        List<RecipeEntity> recipeEntities = likeEntities.stream()
                .map(LikeEntity::getRecipe)
                .toList();

        List<RecipeEntity> filteredRecipeEntities = recipeEntities.stream()
                .filter(recipt -> {
                    if(user.getCookingBudget() >= recipt.getOneBudget()) {
                        System.out.println(user.getIngredients().stream().toList());
                        return countOverlappingIngredients(user.getIngredients(), recipt.getIngredients()) > 3;
                    }
                    return false;
                }).toList();

        return filteredRecipeEntities.stream()
                .map(RecipeResponseDTO.list::toDto)
                .toList();
    }

    // 두 재료 리스트에서 겹치는 재료 수 카운트
    private int countOverlappingIngredients(List<IngredientEntity> ingredients1, List<RecipeIngredientEntity> ingredients2) {
        Set<String> uniqueIngredients1 = ingredients1.stream()
                .map(ingredient -> extractFirstWord(ingredient.getIngredient()))
                .collect(Collectors.toSet());

        Set<String> uniqueIngredients2 = ingredients2.stream()
                .map(ingredient -> extractFirstWord(ingredient.getIngredient()))
                .collect(Collectors.toSet());

        // 중복된 재료 수 카운트
        uniqueIngredients1.retainAll(uniqueIngredients2);

        return uniqueIngredients1.size();
    }

    private String extractFirstWord(String ingredient) {
        if (ingredient == null || ingredient.trim().isEmpty()) {
            return "";
        }
        // 공백을 기준으로 첫 번째 단어를 추출
        return ingredient.split("\\s+")[0];
    }
}
