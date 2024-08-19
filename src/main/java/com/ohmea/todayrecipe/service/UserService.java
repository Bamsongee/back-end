package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.recipe.RecipeResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.dto.user.JoinDTO;
import com.ohmea.todayrecipe.dto.user.UpdateUserDTO;
import com.ohmea.todayrecipe.dto.user.UserResponseDTO;
import com.ohmea.todayrecipe.entity.*;
import com.ohmea.todayrecipe.exception.EntityDuplicatedException;
import com.ohmea.todayrecipe.exception.LikeNotFoundException;
import com.ohmea.todayrecipe.exception.RecipeNotFoundException;
import com.ohmea.todayrecipe.repository.LikeRepository;
import com.ohmea.todayrecipe.repository.RecipeRepository;
import com.ohmea.todayrecipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseDTO<String> joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        GenderEnum gender = joinDTO.getGender();
        Integer cookingBudget = joinDTO.getCookingBudget();
        CookingSkillEnum cookingSkill = joinDTO.getCookingSkill();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            throw new EntityDuplicatedException("중복된 아이디가 존재합니다.");
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .gender(gender)
                .cookingSkill(cookingSkill)
                .cookingBudget(cookingBudget)
                .role("ROLE_ADMIN")
                .build();

        userRepository.save(user);

        return new ResponseDTO<>(HttpStatus.CREATED.value(), "회원가입 성공", null);
    }

    public UserResponseDTO getUserInfo(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        return UserResponseDTO.builder()
                .username(user.getUsername())
                .gender(user.getGender())
                .cookingBudget(user.getCookingBudget())
                .cookingSkill(user.getCookingSkill())
                .build();
    }

    @Transactional
    public UserResponseDTO updateUser(String username, UpdateUserDTO updateUserDTO) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        user.updateUser(updateUserDTO.getCookingSkill(), updateUserDTO.getCookingBudget());

        userRepository.save(user);

        return UserResponseDTO.builder()
                .username(user.getUsername())
                .gender(user.getGender())
                .cookingBudget(user.getCookingBudget())
                .cookingSkill(user.getCookingSkill())
                .build();
    }

    // 레시피 찜
    @Transactional
    public String likeRecipe(String username, Long id) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));
        RecipeEntity recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다: " + id));

        boolean alreadyLiked = likeRepository.existsByUserAndRecipe(user, recipe);
        if (alreadyLiked) {
            return "이미 찜한 레시피입니다.";
        }

        LikeEntity like = LikeEntity.builder()
                .user(user)
                .recipe(recipe)
                .build();
        likeRepository.save(like);

        return "레시피 찜이 완료되었습니다.";
    }


    // 레시피 찜 해제
    @Transactional
    public void unlikeRecipe(String username, Long id) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        RecipeEntity recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("해당 레시피를 찾을 수 없습니다: " + id));

        LikeEntity like = likeRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new LikeNotFoundException("해당 레시피는 찜한 상태가 아닙니다: " + id));

        likeRepository.delete(like);
    }


    // 찜 레시피 조회
    public List<RecipeResponseDTO.list> getLikedRecipes(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        return user.getLikes().stream()
                .map(like -> RecipeResponseDTO.list.toDto(like.getRecipe()))
                .collect(Collectors.toList());
    }

    // 찜 레시피 카테고리별 필터링
    public List<RecipeResponseDTO.list> getLikedRecipesByCategory(String username, String category) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        // 사용자의 모든 찜 레시피 가져오기
        List<RecipeEntity> recipeEntities = user.getLikes().stream()
                .map(LikeEntity::getRecipe).toList();

        // 카테고리 필터링
        List<RecipeEntity> filteredRecipes = recipeEntities.stream()
                .filter(recipe -> recipe.getCategory().equals(category))
                .toList();

        // DTO로 변환하여 응답
        return filteredRecipes.stream()
                .map(RecipeResponseDTO.list::toDto)
                .toList();
    }

    // (알고리즘) 찜한 레시피
    public List<RecipeResponseDTO.list> getTopCategoryRecipes(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        List<LikeEntity> likedRecipes = user.getLikes();

        if (likedRecipes.isEmpty()) {
            return Collections.emptyList();
        }

        // 카테고리별 카운트
        Map<String, Long> categoryCount = likedRecipes.stream()
                .collect(Collectors.groupingBy(like -> like.getRecipe().getCategory(), Collectors.counting()));

        String topCategory = Collections.max(categoryCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        // 해당 카테고리 레시피 10개 조회
        List<RecipeEntity> topCategoryRecipes = recipeRepository.findByCategory(topCategory)
                .stream()
                .limit(10)
                .toList();

        return topCategoryRecipes.stream()
                .map(RecipeResponseDTO.list::toDto)
                .collect(Collectors.toList());
    }

    // (알고리즘) 조회수 레시피
    public List<RecipeResponseDTO.list> getTopRecipesByGender(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        GenderEnum gender = user.getGender();

        List<RecipeEntity> topRecipes;
        if (gender == GenderEnum.WOMAN) {
            topRecipes = recipeRepository.findTop10ByOrderByWomanCountDesc();
        } else if (gender == GenderEnum.MAN) {
            topRecipes = recipeRepository.findTop10ByOrderByManCountDesc();
        } else {
            topRecipes = Collections.emptyList();
        }

        return topRecipes.stream()
                .map(RecipeResponseDTO.list::toDto)
                .collect(Collectors.toList());
    }

    // (알고리즘) 두 개 경로 합치기
    public List<RecipeResponseDTO.list> getCombinedRecommendations(String username) {
        // 카테고리 추천에서 10개 가져오기
        List<RecipeResponseDTO.list> categoryRecommendations = getTopCategoryRecipes(username)
                .stream()
                .limit(10)
                .collect(Collectors.toList());

        // 조회수 추천에서 20개 가져오기
        List<RecipeResponseDTO.list> genderRecommendations = getTopRecipesByGender(username)
                .stream()
                .limit(20)
                .collect(Collectors.toList());

        // 합치기
        categoryRecommendations.addAll(genderRecommendations);

        return categoryRecommendations;
    }
}
