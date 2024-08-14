package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    List<CommentEntity> findByRecipe_Ranking(String ranking);

    // 유저 댓글 조회
    @Query("SELECT c FROM CommentEntity c WHERE c.user.username = :username")
    List<CommentEntity> findByUsername(@Param("username") String username);
}