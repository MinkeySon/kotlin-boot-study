package com.example.demo.data.dto

data class UserCreateDto (
    val name: String? = "알 수 없음",
    val email: String? = null,
    val id: String,
    val password: String
)