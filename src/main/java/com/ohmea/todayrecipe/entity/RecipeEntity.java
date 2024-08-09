package com.ohmea.todayrecipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntity {
    @Id
    private String ranking;
    private String name;
    private String link;
    private String imgURL;
    @Column(columnDefinition = "TEXT")
    private String ingredients;
    @Column(columnDefinition = "TEXT")
    private String recipe;
    private String serving;
    private String time;
    private String difficulty;
    private String keyword;
    private String category;
    private Integer oneBudget;
    private Integer totalBudget;


    @Column(nullable = false, columnDefinition = "int default 0")
    private int viewCount;  // 조회수 필드 추가

    public void incrementViewCount() {
        this.viewCount++;
    }

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("id asc") // 댓글 정렬
    private List<CommentEntity> comments;

}
