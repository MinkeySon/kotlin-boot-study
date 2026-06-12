package com.example.demo.data.dto

import com.example.demo.data.domain.CommonResponse

data class AuthResponseDto (
    val accessToken: String,
    val status: CommonResponse<*>
){
    companion object {
        fun response(token: String, status: CommonResponse<*>) : AuthResponseDto{
            return AuthResponseDto(
                accessToken = token,
                status = status
            )
        }
    }
}