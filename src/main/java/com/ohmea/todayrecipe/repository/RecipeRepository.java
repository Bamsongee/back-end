package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.RecipeEntity;
import io.netty.util.internal.ObjectPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, String> {
    List<RecipeEntity> findByNameContainingIgnoreCase(String name);
    Optional<RecipeEntity> findById(Long id);

    // 찜 알고리즘
    List<RecipeEntity> findByCategory(String category);

    // 조회수 알고리즘
    List<RecipeEntity> findTop10ByOrderByWomanCountDesc();
    List<RecipeEntity> findTop10ByOrderByManCountDesc();

    // 전체 조회수
    List<RecipeEntity> findTop10ByOrderByTotalCountDesc();

}

