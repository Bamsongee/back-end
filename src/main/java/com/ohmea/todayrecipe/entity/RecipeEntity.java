package com.ohmea.todayrecipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private String ingredients;
    @Column(columnDefinition = "TEXT")
    private String recipe;
    private String serving;
    private String time;
    private String difficulty;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommentEntity> comments = new ArrayList<>();
}
