package com.ohmea.todayrecipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private final boolean success = false;
    private int status;
    private String message;
}
