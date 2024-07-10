package com.ohmea.todayrecipe.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS_CONNECT(HttpStatus.OK, "커스텀 예외처리 성공"),
    ;

    private final HttpStatus status;
    private final String message;
}