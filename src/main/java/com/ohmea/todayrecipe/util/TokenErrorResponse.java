package com.ohmea.todayrecipe.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohmea.todayrecipe.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;

public class TokenErrorResponse {
    public static void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), errorMessage);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setStatus(HttpStatus.BAD_REQUEST.value());

        PrintWriter writer = response.getWriter();
        writer.print(jsonResponse);
        writer.flush();
        writer.close();
    }
}
