package com.ohmea.todayrecipe.util;

import com.ohmea.todayrecipe.entity.RecipeEntity;
import com.ohmea.todayrecipe.entity.RecipeIngredientEntity;
import com.opencsv.bean.CsvToBeanBuilder;
import com.ohmea.todayrecipe.entity.ProductEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvReader {

    // 할인 상품 읽기
    public List<ProductEntity> readProductsFromCsv(String filePath) {
        try (Reader reader = new InputStreamReader(new ClassPathResource(filePath).getInputStream())) {
            return new CsvToBeanBuilder<ProductEntity>(reader)
                    .withType(ProductEntity.class)
                    .build()
                    .parse();
        } catch (Exception e) {
            throw new RuntimeException("CSV 파일 읽기 중 에러 발생", e);
        }
    }

    // 레시피 읽기
    public List<RecipeEntity> readRecipesFromCsv(String filePath) {
        try (Reader reader = new InputStreamReader(new ClassPathResource(filePath).getInputStream())) {
            return new CsvToBeanBuilder<RecipeEntity>(reader)
                    .withType(RecipeEntity.class)
                    .build()
                    .parse();
        } catch (Exception e) {
            throw new RuntimeException("CSV 파일 읽기 중 에러 발생", e);
        }
    }
}
