package com.example.demo.config

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// http://localhost:8080/swagger-ui/index.html
@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("Kotlin Boot Study API")
                    .description("코틀린 스프링부트 스터디 API 문서")
                    .version("v1.0.0")
            )
}