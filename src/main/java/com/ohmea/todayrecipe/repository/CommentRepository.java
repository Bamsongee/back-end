package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    List<CommentEntity> findByRecipe_Ranking(String ranking);
}