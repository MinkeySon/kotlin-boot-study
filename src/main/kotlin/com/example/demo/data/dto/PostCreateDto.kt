package com.example.demo.data.dto

import com.example.demo.data.domain.User

data class PostCreateDto(
    val title: String,
    val content: String,
    val user: User,
) {
}