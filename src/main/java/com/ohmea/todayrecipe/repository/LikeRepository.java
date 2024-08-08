package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
}
