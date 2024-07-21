package com.ohmea.todayrecipe.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohmea.todayrecipe.dto.response.ErrorResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.entity.RefreshEntity;
import com.ohmea.todayrecipe.repository.RefreshRedisRepository;
import com.ohmea.todayrecipe.util.TokenErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;

    private final RefreshRedisRepository refreshRedisRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // path and method 체크
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        // refresh token
        String refreshToken = request.getHeader("refresh");

        if (refreshToken == null) {
            TokenErrorResponse.sendErrorResponse(response, "헤더에 refresh 토큰이 없습니다.");
            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            TokenErrorResponse.sendErrorResponse(response, "토큰이 만료되었습니다.");
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getType(refreshToken);
        if (!category.equals("refreshToken")) {
            TokenErrorResponse.sendErrorResponse(response, "refresh 토큰이 아닙니다.");
            return;
        }

        // redis에 저장되어 있는지 확인
        Optional<RefreshEntity> isExist = refreshRedisRepository.findById(refreshToken);
        if (isExist.isEmpty()) {
            TokenErrorResponse.sendErrorResponse(response, "토큰이 DB에 존재하지 않습니다.");
        }

        //로그아웃 진행
        //Refresh 토큰 redis에서 제거
        refreshRedisRepository.deleteById(refreshToken);

        // response
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.OK.value(), "성공적으로 로그아웃되었습니다.", null);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseDTO);
        PrintWriter writer = response.getWriter();
        writer.print(jsonResponse);
        writer.flush();
        writer.close();
    }

}
