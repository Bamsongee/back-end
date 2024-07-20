package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.service.KurlyCrollingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/kurly")
@RequiredArgsConstructor
public class KurlyCrollingController {
    private final KurlyCrollingService kurlyCrollingService;
    @GetMapping()
    public ResponseDTO<List<HashMap<String, Object>>> kurlyCrolling() throws Exception {
        return kurlyCrollingService.curlyCrolling();
    }
}
