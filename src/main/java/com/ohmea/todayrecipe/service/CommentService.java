package com.ohmea.todayrecipe.service;
import com.ohmea.todayrecipe.entity.CommentEntity;
import com.ohmea.todayrecipe.entity.RecipeEntity;
import com.ohmea.todayrecipe.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.ohmea.todayrecipe.repository.CommentRepository;

import java.util.List;

public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<CommentEntity> getCommentsByRecipeId(String recipeId) {
        return commentRepository.findByRecipe(recipeId);
    }

    public CommentEntity saveComment(String content, UserEntity user, String recipeId) {
        CommentEntity comment = CommentEntity.builder()
                .comment(content)
                .user(user)
                .recipe(recipeId)
                .build();
        return commentRepository.save(comment);
    }

}

