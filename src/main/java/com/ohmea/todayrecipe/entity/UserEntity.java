package com.ohmea.todayrecipe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 닉네임
    private String username;
    // 비밀번호
    private String password;
    // 성별
    private GenderEnum gender;
    // 요리 실력
    private CookingSkillEnum cookingSkill;
    // 예산
    private Integer cookingBudget;
    // admin
    private String role;
    // refrigerator 식재료들
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IngredientEntity> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LikeEntity> likes = new ArrayList<>();

    // user 정보 업데이트
    public void updateUser(CookingSkillEnum cookingSkill, Integer cookingBudget) {
        this.cookingSkill = cookingSkill == null ? this.cookingSkill : cookingSkill;
        this.cookingBudget = cookingBudget == null ? this.cookingBudget : cookingBudget;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommentEntity> comments = new ArrayList<>();

}