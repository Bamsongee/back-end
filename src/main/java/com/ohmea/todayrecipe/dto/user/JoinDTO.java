package com.ohmea.todayrecipe.dto.user;

import com.ohmea.todayrecipe.entity.CookingSkillEnum;
import com.ohmea.todayrecipe.entity.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDTO {

    private String username;
    private String password;
    private GenderEnum gender;
    private Integer age;
    private CookingSkillEnum cookingSkill;
    private Integer cookingBudget;
    private String filter;
}
