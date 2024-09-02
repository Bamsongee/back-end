package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.IngredientEntity;
import com.ohmea.todayrecipe.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
    List<IngredientEntity> findByUserOrderByIdDesc(UserEntity user);
    Boolean existsByIngredientAndUser(String ingredient, UserEntity user);
}
