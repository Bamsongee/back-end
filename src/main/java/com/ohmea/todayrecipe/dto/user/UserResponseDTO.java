package com.ohmea.todayrecipe.dto.user;

import com.ohmea.todayrecipe.entity.CookingSkillEnum;
import com.ohmea.todayrecipe.entity.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private GenderEnum gender;
    private Integer age;
    private CookingSkillEnum cookingSkill;
    private Integer cookingBudget;
    private String filter;
}
