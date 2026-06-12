package com.example.demo.config

import com.example.demo.data.domain.CommonResponse
import com.example.demo.data.domain.ResultCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler (

) : AccessDeniedHandler {
    private val log = logger()
    private val objectMapper: ObjectMapper = ObjectMapper()
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.error{"[JwtAccessDeniedHandler::handle] access denied!"}
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.status = ResultCode.UNAUTHORIZED.code
        response.writer.write(
            objectMapper.writeValueAsString(
                CommonResponse<Unit>(code = ResultCode.UNAUTHORIZED.code, msg = ResultCode.UNAUTHORIZED.msg)
            )
        )
    }


}