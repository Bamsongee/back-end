package com.ohmea.todayrecipe.dto.user;

import com.ohmea.todayrecipe.entity.CookingSkillEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private CookingSkillEnum cookingSkill;
    private Integer cookingBudget;
    private String filter;
}
