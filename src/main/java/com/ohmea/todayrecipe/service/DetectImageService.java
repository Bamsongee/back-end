package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.detectimage.DetectImageResponseDTO;
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

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetectImageService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final IngredientRepository refrigeratorRepository;

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

        detectedItems.forEach(item -> {
            IngredientEntity refrigeratorEntity = IngredientEntity.builder()
                    .ingredient(item)
                    .count(1) // 사용자가 추후 변경할 수 있도록 해야 함
                    .user(user)
                    .build();

            refrigeratorRepository.save(refrigeratorEntity);
        });

        return DetectImageResponseDTO.builder()
                .ingredients(detectedItems)
                .build();
    }
}
