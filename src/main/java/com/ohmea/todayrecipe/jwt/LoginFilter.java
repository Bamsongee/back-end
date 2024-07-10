package com.ohmea.todayrecipe.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohmea.todayrecipe.dto.user.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 클라이언트 요청 중간에 username과 password를 가져옴
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // 테스트
        System.out.println(username);

        // 스프링 시큐리티에서 username, password 검증하기 위해 token에 담기
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // token에 담은 검증을 위한 AuthenticationManager로 전달 > 검증을 진행
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // UserDetails
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // 정보 가져오기(id, username, role)
        String username = customUserDetails.getUsername();
        Long userId = customUserDetails.getId();
        System.out.println(userId);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 토큰 생성
        String token = jwtUtil.createJwt("self", "access", username, role, 60*60*10L); // 24시간(하루)

        // access 토큰 설정
        response.setHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
