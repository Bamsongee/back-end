package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.ingredient.IngredientResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO> getIngredients() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<IngredientResponseDTO> response = ingredientService.getIngredients(username);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO(200, "모든 냉장고 데이터 조회 완료", response));
    }
}
