package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.detectimage.DetectImageResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.service.DetectImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/detection")
@RequiredArgsConstructor
public class DetectImageController {
    private final DetectImageService detectImageService;
    @PostMapping
    public ResponseEntity<ResponseDTO> getDetectImage(
            @RequestParam("file") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        DetectImageResponseDTO response = detectImageService.getDetectedImage(username, file);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO(200, "사진 분석 완료", response));
    }
}
