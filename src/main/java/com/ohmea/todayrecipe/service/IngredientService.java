package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.ingredient.IngredientResponseDTO;
import com.ohmea.todayrecipe.entity.IngredientEntity;
import com.ohmea.todayrecipe.entity.UserEntity;
import com.ohmea.todayrecipe.repository.IngredientRepository;
import com.ohmea.todayrecipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public List<IngredientResponseDTO> getIngredients(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        List<IngredientEntity> ingredientEntityList = ingredientRepository.findByUser(user);
        List<IngredientResponseDTO> ingredientResponseDTOList = new ArrayList<>();

        ingredientEntityList.forEach(entity -> {
            IngredientResponseDTO ingredientResponseDTO = IngredientResponseDTO.toDto(entity);
            ingredientResponseDTOList.add(ingredientResponseDTO);
        });

        return ingredientResponseDTOList;
    }
}
