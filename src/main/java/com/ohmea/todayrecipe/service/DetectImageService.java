package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.detectimage.DetectImageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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

    public DetectImageResponseDTO getDetectedImage(MultipartFile file) {
        String url = "http://15.165.241.166/detection";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String[].class);

        List<String> detectedItems = Arrays.asList(response.getBody());

        return DetectImageResponseDTO.builder()
                .ingredients(detectedItems)
                .build();
    }
}
