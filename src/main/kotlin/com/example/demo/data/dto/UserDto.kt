package com.example.demo.data.dto

import com.example.demo.data.domain.User

data class UserDto (
    val name: String,
    val email: String?,
    val uid: Long,
){
    companion object {
        fun toDto(user: User): UserDto = UserDto(
            name = user.name,
            email = user.email,
            uid = user.uid ?:0L,
        )
    }
}