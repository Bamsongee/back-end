package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.dto.user.JoinDTO;
import com.ohmea.todayrecipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService joinService;

    @GetMapping("/")
    public ResponseDTO<String> init(){
        return new ResponseDTO<String>(HttpStatus.OK.value(), "배포 성공", null);
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDTO<String>> joinProcess(JoinDTO joinDTO) {

        ResponseDTO<String> response = joinService.joinProcess(joinDTO);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("/login-test")
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return "Username: " + username;
    }
}
