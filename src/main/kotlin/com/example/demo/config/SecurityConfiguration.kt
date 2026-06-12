package com.example.demo.config

import org.springframework.security.config.annotation.web.invoke
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfiguration (
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
){
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOriginPatterns = listOf("*")     // ⚠ allowedOrigins가 아님 — 아래 설명
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            exposedHeaders = listOf("*")
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            cors { configurationSource = corsConfigurationSource() }
            formLogin { disable() }
            httpBasic { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            exceptionHandling {
                authenticationEntryPoint = jwtAuthenticationEntryPoint
                accessDeniedHandler = jwtAccessDeniedHandler
            }
            authorizeHttpRequests {
                PATTERNS.forEach { authorize(it, permitAll) }
                authorize("/api/**", hasRole("USER"))
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
        }
        return http.build()
    }

    companion object {
        val PATTERNS = arrayOf(
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/css/**",
            "/images/**", "/js/**", "/favicon.ico", "/api/user/auth/**",
        )
    }

}