package com.ohmea.todayrecipe.controller;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
public class TestController {
    @GetMapping("/")
    public ResponseDTO<String> init(){
        return new ResponseDTO<String>(HttpStatus.OK.value(), "배포 성공", null);
    }
}
