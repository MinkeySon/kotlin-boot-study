package com.example.demo.config

import com.example.demo.data.domain.CommonResponse
import com.example.demo.data.domain.ResultCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint(

): AuthenticationEntryPoint {

    private val objectMapper: ObjectMapper = ObjectMapper()

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val result = request.getAttribute("error") as? ResultCode ?: ResultCode.EXPIRED_TOKEN

        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.status = 401
        response.writer.write(
            objectMapper.writeValueAsString(
                CommonResponse<Unit>(code = 401, msg = result.msg)
            )
        )
    }
}