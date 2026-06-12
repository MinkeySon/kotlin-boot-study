package com.example.demo.config

import com.example.demo.data.domain.ResultCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter (
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: StringRedisTemplate,
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
    private val log = logger()
    protected override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return passUrl.any { pathMatcher.match(it, request.servletPath) }
    }

    protected override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwtTokenProvider.extractToken(request)

        if (!token.isNullOrEmpty() && jwtTokenProvider.validateToken(token)) {
            if (redisTemplate.hasKey("blacklist:$token")) {
                request.setAttribute("error", ResultCode.UNAUTHORIZED)
                log.warn {"[JwtAuthenticationFilter::doFilterInternal] this is logout token!"}
            } else {
                try {
                    val authentication = jwtTokenProvider.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                } catch (ex: Exception) {
                    log.warn(ex) { "[JwtAuthenticationFilter::doFilterInternal] authentication failed" }
                    request.setAttribute("error", ResultCode.UNAUTHORIZED)
                }
            }
        }

        filterChain.doFilter(request, response)
    }

}