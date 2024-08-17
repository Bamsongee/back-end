package com.ohmea.todayrecipe.service;
import com.ohmea.todayrecipe.dto.comment.CommentResponseDTO;
import com.ohmea.todayrecipe.dto.comment.CreateCommentDTO;
import com.ohmea.todayrecipe.entity.CommentEntity;
import com.ohmea.todayrecipe.entity.RecipeEntity;
import com.ohmea.todayrecipe.entity.UserEntity;
import com.ohmea.todayrecipe.exception.RecipeNotFoundException;
import com.ohmea.todayrecipe.repository.RecipeRepository;
import com.ohmea.todayrecipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.ohmea.todayrecipe.repository.CommentRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public List<CommentResponseDTO> getCommentsByRecipeRanking(String ranking) {
        List<CommentEntity> commentEntityList = commentRepository.findByRecipe_Ranking(ranking);
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        commentEntityList.forEach(entity -> {
            commentResponseDTOList.add(CommentResponseDTO.toDto(entity));
        });
        return commentResponseDTOList;
    }

    public void saveComment(String content, Long id, String username) {
        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("레시피를 찾을 수 없습니다. "));

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        CommentEntity comment = CommentEntity.builder()
                .comment(content)
                .user(userEntity)
                .recipe(recipeEntity)
                .build();

        commentRepository.save(comment);
    }

    public List<CommentResponseDTO> getCommentsByUser(String username) {
        List<CommentEntity> commentEntityList = commentRepository.findByUsername(username);
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        commentEntityList.forEach(entity -> {
            commentResponseDTOList.add(CommentResponseDTO.toDto(entity));
        });
        return commentResponseDTOList;
    }
}

