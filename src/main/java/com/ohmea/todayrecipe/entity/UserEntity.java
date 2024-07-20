package com.ohmea.todayrecipe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
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
    private Integer CookingBudget;
    // 맞춤 필터링 제공
    private boolean customFilter;
    // admin
    private String role;
}