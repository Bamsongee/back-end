package com.ohmea.todayrecipe.dto.comment;

import com.ohmea.todayrecipe.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String comment;
    private String username;
    private LocalDateTime createdAt;
    private Long recipeId;

    public static CommentResponseDTO toDto(CommentEntity comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .username(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .recipeId(comment.getRecipe().getId())
                .build();
    }
}
