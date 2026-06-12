package com.example.demo.service

import com.example.demo.data.dto.SignInDto
import com.example.demo.data.dto.UserCreateDto
import com.example.demo.data.dto.UserUpdateDto
import org.springframework.http.ResponseEntity
import java.net.http.HttpHeaders

interface UserService {
    fun saveUser(dto: UserCreateDto): ResponseEntity<*>

    fun getUserList(): ResponseEntity<*>

    fun updateUser(dto: UserUpdateDto): ResponseEntity<*>

    fun deleteUser(id: Long): ResponseEntity<*>

    fun signUp(dto: UserCreateDto): ResponseEntity<*>

    fun signIn(dto: SignInDto): ResponseEntity<*>

    fun logOut(token: String): ResponseEntity<*>
}