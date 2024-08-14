package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.recipe.RecipeResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RecipeResponseDTO.list>>> getAllRecipes() {
        List<RecipeResponseDTO.list> response = recipeService.getAllRecipes();
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<List<RecipeResponseDTO.list>>(200, "레시피 전체 조회 완료", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<RecipeResponseDTO.list>>> searchRecipes(@RequestParam String name) {
        List<RecipeResponseDTO.list> response = recipeService.searchRecipesByName(name);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<List<RecipeResponseDTO.list>>(200, "레시피 검색 완료", response));
    }

    /*
    @GetMapping("/relation")
    public ResponseEntity<ResponseDTO<List<RecipeResponseDTO>>> findRelatedRecipes(@RequestParam String name) {
        List<RecipeResponseDTO> response = recipeService.findRelatedRecipesByIngredients(name);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<List<RecipeResponseDTO>>(200, "연관 레시피 조회 완료", response));
    } */

    //레시피 세부 조회
    @GetMapping("/detail/{ranking}")
    public ResponseEntity<ResponseDTO<RecipeResponseDTO>> getRecipeDetail(@PathVariable String ranking) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        RecipeResponseDTO response = recipeService.getRecipeByRanking(ranking, username);
        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND.value())
                    .body(new ResponseDTO<>(404, "레시피를 찾을 수 없습니다.", null));
        }
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "레시피 상세 조회 완료", response));
    }

    // 요리 가능한 레시피 출력
    @GetMapping("/possible")
    public ResponseEntity<ResponseDTO> getRecipeDetail() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<RecipeResponseDTO.list> response = recipeService.getPosibleRecipes(username);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "예산, 재료 범위 내 요리 가능한 레시피 상세 조회 완료", response));
    }

    // 인기 레시피 조회
    @GetMapping("/popular")
    public ResponseEntity<ResponseDTO<List<RecipeResponseDTO.list>>> getTopPopularRecipes() {
        List<RecipeResponseDTO.list> response = recipeService.getTop10PopularRecipes();
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "인기 레시피 조회 완료", response));
    }
}