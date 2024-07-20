package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, String> {
}
