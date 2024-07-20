package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.entity.ProductEntity;
import com.ohmea.todayrecipe.entity.RecipeEntity;
import com.ohmea.todayrecipe.repository.RecipeRepository;
import com.ohmea.todayrecipe.util.CsvReader;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired private CsvReader csvReader;
    private static final String CSV_FILE_PATH = "static/recipe_entity.csv";

    @PostConstruct
    public void init() {
        List<RecipeEntity> recipes = csvReader.readRecipesFromCsv(CSV_FILE_PATH);
        recipeRepository.saveAll(recipes);
    }

    public List<RecipeEntity> getAllRecipes() {
        return recipeRepository.findAll();
    }
}
