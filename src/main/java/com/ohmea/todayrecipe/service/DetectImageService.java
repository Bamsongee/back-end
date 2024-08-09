package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.ingredient.DetectImageResponseDTO;
import com.ohmea.todayrecipe.dto.ingredient.IngredientExistsResponseDTO;
import com.ohmea.todayrecipe.entity.IngredientEntity;
import com.ohmea.todayrecipe.entity.UserEntity;
import com.ohmea.todayrecipe.repository.IngredientRepository;
import com.ohmea.todayrecipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetectImageService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    @Transactional
    public DetectImageResponseDTO getDetectedImage(String username, MultipartFile file) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        String url = "http://15.165.241.166/detection";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String[].class);

        List<String> detectedItems = Arrays.asList(response.getBody());
        List<IngredientExistsResponseDTO> ingredientExistsResponseDTOS = new ArrayList<>();

        detectedItems.forEach(item -> {
            boolean isExists = ingredientRepository.existsByIngredient(item);
            if(!isExists) {
                IngredientEntity refrigeratorEntity = IngredientEntity.builder()
                        .ingredient(item)
                        .user(user)
                        .build();

                ingredientRepository.save(refrigeratorEntity);
            }

            ingredientExistsResponseDTOS.add(new IngredientExistsResponseDTO(item, isExists));

        });

        return DetectImageResponseDTO.builder()
                .results(ingredientExistsResponseDTOS)
                .build();
    }
}
