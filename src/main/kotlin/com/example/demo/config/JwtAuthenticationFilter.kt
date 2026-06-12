package com.example.demo.config

import com.example.demo.data.domain.ResultCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter (
    private val jwtTokenProvider: JwtTokenProvider,
    private val passUrl: List<String> = listOf(
        "/api/user/auth/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/css/**",
        "/images/**",
        "/js/**",
        "/favicon.ico",
    )
): OncePerRequestFilter() {

    private val pathMatcher: AntPathMatcher = AntPathMatcher()

    protected override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return passUrl.any { pathMatcher.match(it, request.servletPath) }
    }

    protected override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwtTokenProvider.extractToken(request)

        if(!token.isEmpty() && jwtTokenProvider.validateToken(token)){
            try{
                val authentication: Authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            } catch(ex: Exception){
                request.setAttribute("error", ResultCode.FAIL)
            }
        }

        filterChain.doFilter(request, response)
    }

}