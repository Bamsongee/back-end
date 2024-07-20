package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.product.ProductResponseDTO;
import com.ohmea.todayrecipe.dto.recipe.RecipeResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.entity.RecipeEntity;
import com.ohmea.todayrecipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RecipeResponseDTO>>> getAllRecipes() {

        List<RecipeResponseDTO> response = recipeService.getAllRecipes();

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<List<RecipeResponseDTO>>(200, "레시피 전체 조회 완료", response));
    }
}
