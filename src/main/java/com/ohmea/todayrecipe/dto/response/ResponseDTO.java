package com.ohmea.todayrecipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
    private final boolean success = true;
    private Integer status;
    private String message;
    private T data;
}
