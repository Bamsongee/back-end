package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.dto.user.JoinDTO;
import com.ohmea.todayrecipe.exception.AccessTokenExpiredException;
import com.ohmea.todayrecipe.exception.NotRefreshTokenException;
import com.ohmea.todayrecipe.exception.TokenNotFoundException;
import com.ohmea.todayrecipe.jwt.JWTUtil;
import com.ohmea.todayrecipe.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    private final JWTUtil jwtUtil;

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

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDTO> reissue(HttpServletRequest request, HttpServletResponse response) {
        // 헤더에서 refresh키에 담긴 토큰을 꺼냄
        String refreshToken = request.getHeader("refresh");

        if (refreshToken == null) {
            throw new TokenNotFoundException("헤더에서 토큰을 찾을 수 없습니다.");
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException("access 토큰이 만료되었습니다.");
        }

        String type = jwtUtil.getType(refreshToken);
        if (!type.equals("refreshToken")) {
            throw new NotRefreshTokenException("refesh token이 아닙니다.");
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 새로운 Access token과 refreshToken 생성
        String newAccessToken = jwtUtil.createJwt("accessToken", username, role, 600000L);
        String newRefreshToken = jwtUtil.createJwt("refreshToken", username, role, 600000L);

        response.setHeader("accessToken", "Bearer " + newAccessToken);
        response.setHeader("refreshToken", "Bearer " + newRefreshToken);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "accessToken 재발급 완료. 헤더를 확인하세요.", null));
    }


    @GetMapping("/login-test")
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return "Username: " + username;
    }
}
