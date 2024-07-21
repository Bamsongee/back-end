package com.ohmea.todayrecipe.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohmea.todayrecipe.dto.response.ErrorResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.dto.user.CustomUserDetails;
import com.ohmea.todayrecipe.entity.RefreshEntity;
import com.ohmea.todayrecipe.repository.AuthRepositoryWithRedis;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final AuthRepositoryWithRedis authRepositoryWithRedis;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();


        String accesstoken = jwtUtil.createJwt("accessToken", username, role, 86400000L);
        String refreshToken = jwtUtil.createJwt("refreshToken", username, role, 86400000L);

        RefreshEntity refreshEntity = new RefreshEntity(refreshToken, username);
        authRepositoryWithRedis.save(refreshEntity);

        response.addHeader("accessToken", "Bearer " + accesstoken);
        response.addHeader("refreshToken", "Bearer " + refreshToken);

        ResponseDTO<String> responseDTO = new ResponseDTO<>(HttpStatus.OK.value(), "로그인 성공, 헤더 토큰을 확인하세요.", null);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseDTO);
        response.getWriter().write(jsonResponse);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        response.setStatus(401);

        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "로그인 실패");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseDTO);
        response.getWriter().write(jsonResponse);
    }
}

