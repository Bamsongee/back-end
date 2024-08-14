package com.ohmea.todayrecipe.entity;

import com.ohmea.todayrecipe.util.IngredientListConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "ranking")
    private String ranking;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "link")
    private String link;

    @CsvBindByName(column = "imgURL")
    private String imgURL;

    @CsvBindByName(column = "recipe")
    @Column(columnDefinition = "TEXT")
    private String recipe;

    @CsvBindByName(column = "serving")
    private String serving;

    @CsvBindByName(column = "time")
    private String time;

    @CsvBindByName(column = "difficulty")
    private String difficulty;

    @CsvBindByName(column = "keyword")
    private String keyword;

    @CsvBindByName(column = "category")
    private String category;

    @CsvBindByName(column = "oneBudget")
    private Integer oneBudget;

    @CsvBindByName(column = "totalBudget")
    private Integer totalBudget;

    // 식재료
    @Setter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id")
    @CsvCustomBindByName(converter = IngredientListConverter.class)
    private List<RecipeIngredientEntity> ingredients;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int manCount;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int womanCount;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int totalCount;

    public void incrementViewCountByGender(GenderEnum gender) {
        if (gender == GenderEnum.MAN) {
            this.manCount++;
            this.totalCount++;
        } else if (gender == GenderEnum.WOMAN) {
            this.womanCount++;
            this.totalCount++;
        }
    }

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("id asc") // 댓글 정렬
    private List<CommentEntity> comments;

    // CSV 변환 중 사용
    @Transient
    private String ingredientsString;

}
