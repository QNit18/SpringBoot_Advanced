package com.qnit18.springadvanced.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qnit18.springadvanced.dto.response.ApiResponse;
import com.qnit18.springadvanced.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // Xay ra khi 1 exception xay ra khi authentication fail
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());

        // Xét content trả về là JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

        // nó đảm bảo rằng mọi dữ liệu đã được viết vào response đã được gửi đi và không còn ở trong bộ đệm nữa.
        response.flushBuffer();
    }
}
