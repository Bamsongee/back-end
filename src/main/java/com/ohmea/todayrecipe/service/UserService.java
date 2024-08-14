package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.recipe.RecipeResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.dto.user.JoinDTO;
import com.ohmea.todayrecipe.dto.user.UpdateUserDTO;
import com.ohmea.todayrecipe.dto.user.UserResponseDTO;
import com.ohmea.todayrecipe.entity.*;
import com.ohmea.todayrecipe.exception.EntityDuplicatedException;
import com.ohmea.todayrecipe.repository.LikeRepository;
import com.ohmea.todayrecipe.repository.RecipeRepository;
import com.ohmea.todayrecipe.repository.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RecipeRepository recipeRepository, LikeRepository likeRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.likeRepository = likeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseDTO<String> joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        GenderEnum gender = joinDTO.getGender();
        Integer age = joinDTO.getAge();
        Integer cookingBudget = joinDTO.getCookingBudget();
        String filter = joinDTO.getFilter();
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
                .filter(filter)
                .age(age)
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
                .age(user.getAge())
                .cookingBudget(user.getCookingBudget())
                .cookingSkill(user.getCookingSkill())
                .filter(user.getFilter())
                .build();
    }

    @Transactional
    public UserResponseDTO updateUser(String username, UpdateUserDTO updateUserDTO) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        user.updateUser(updateUserDTO.getCookingSkill(), updateUserDTO.getCookingBudget(), updateUserDTO.getFilter());

        userRepository.save(user);

        return UserResponseDTO.builder()
                .username(user.getUsername())
                .gender(user.getGender())
                .age(user.getAge())
                .cookingBudget(user.getCookingBudget())
                .cookingSkill(user.getCookingSkill())
                .filter(user.getFilter())
                .build();
    }

    // 레시피 찜
    @Transactional
    public ResponseDTO<String> likeRecipe(String username, String ranking) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));
        RecipeEntity recipe = recipeRepository.findById(ranking)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다: " + ranking));

        boolean alreadyLiked = likeRepository.existsByUserAndRecipe(user, recipe);
        if (alreadyLiked) {
            return new ResponseDTO<>(HttpStatus.OK.value(), "이미 찜한 레시피입니다.", null);
        }

        LikeEntity like = LikeEntity.builder()
                .user(user)
                .recipe(recipe)
                .build();
        likeRepository.save(like);

        return new ResponseDTO<>(HttpStatus.OK.value(), "레시피 찜이 완료되었습니다.", null);
    }


    // 레시피 찜 해제
    @Transactional
    public ResponseDTO<String> unlikeRecipe(String username, String ranking) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        RecipeEntity recipe = recipeRepository.findByRanking(ranking)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다: " + ranking));

        LikeEntity like = likeRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피는 찜한 상태가 아닙니다: " + ranking));

        likeRepository.delete(like);

        return new ResponseDTO<>(HttpStatus.OK.value(), "레시피 찜 해제가 완료되었습니다.", null);
    }


    // 찜 레시피 조회
    public List<RecipeResponseDTO> getLikedRecipes(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        return user.getLikes().stream()
                .map(like -> RecipeResponseDTO.toDto(like.getRecipe()))
                .collect(Collectors.toList());
    }

    // 찜 레시피 카테고리별 필터링
    public List<RecipeResponseDTO> getLikedRecipesByCategory(String username, String category) {
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
                .map(RecipeResponseDTO::toDto)
                .toList();
    }

    // (알고리즘) 찜한 레시피
    public List<RecipeResponseDTO> getTopCategoryRecipes(String username) {
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
                .collect(Collectors.toList());

        return topCategoryRecipes.stream()
                .map(RecipeResponseDTO::toDto)
                .collect(Collectors.toList());
    }

    // (알고리즘) 조회수 레시피
    public List<RecipeResponseDTO> getTopRecipesByGender(String username) {
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
                .map(RecipeResponseDTO::toDto)
                .collect(Collectors.toList());
    }

    // (알고리즘) 두 개 경로 합치기
    public List<RecipeResponseDTO> getCombinedRecommendations(String username) {
        // 카테고리 추천에서 10개 가져오기
        List<RecipeResponseDTO> categoryRecommendations = getTopCategoryRecipes(username)
                .stream()
                .limit(10)
                .collect(Collectors.toList());

        // 조회수 추천에서 20개 가져오기
        List<RecipeResponseDTO> genderRecommendations = getTopRecipesByGender(username)
                .stream()
                .limit(20)
                .collect(Collectors.toList());

        // 합치기
        categoryRecommendations.addAll(genderRecommendations);

        return categoryRecommendations;
    }
}
