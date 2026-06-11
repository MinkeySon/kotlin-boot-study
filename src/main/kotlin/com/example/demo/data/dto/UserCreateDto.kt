package com.example.demo.data.dto

data class UserCreateDto (
    val name: String,
    val email: String? = null,
    val id: String,
    val password: String
)