package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, String> {
    List<RecipeEntity> findByNameContainingIgnoreCase(String name);
}
