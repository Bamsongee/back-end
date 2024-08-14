package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.LikeEntity;
import com.ohmea.todayrecipe.entity.RecipeEntity;
import com.ohmea.todayrecipe.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndRecipe(UserEntity user, RecipeEntity recipe);
    boolean existsByUserAndRecipe(UserEntity user, RecipeEntity recipe);
}
