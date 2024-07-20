package com.ohmea.todayrecipe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 닉네임
    private String username;
    // 비밀번호
    private String password;
    // 성별
    private GenderEnum gender;
    // 나이
    private Integer age;
    // 요리 실력
    private CookingSkillEnum cookingSkill;
    // 예산
    private Integer cookingBudget;
    // 맞춤 필터링 제공
    private String filter;
    // admin
    private String role;
}