package com.ohmea.todayrecipe.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("2024 오늘의 메뉴, 오메 API")
                        .description("2024 오늘의 메뉴, 오메 백엔드 API 명세서")
                        .version("1.0.0"));
    }
}