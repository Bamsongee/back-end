package com.ohmea.todayrecipe.dto.comment;

import com.ohmea.todayrecipe.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String comment;
    private String username;
    private String createdAt;
    private Long recipeId;

    public static CommentResponseDTO toDto(CommentEntity comment) {
        // 날짜/시간 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma")
                .withZone(ZoneId.of("Asia/Seoul"));

        // 시간대 변환 및 포맷 적용
        String formattedDateTime = comment.getCreatedAt()
                .atZone(ZoneId.systemDefault()) // 시스템 기본 시간대
                .withZoneSameInstant(ZoneId.of("Asia/Seoul")) // 한국 시간대로 변환
                .format(formatter);

        return CommentResponseDTO.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .username(comment.getUser().getUsername())
                .createdAt(formattedDateTime)
                .recipeId(comment.getRecipe().getId())
                .build();
    }
}
