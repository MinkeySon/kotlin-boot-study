package com.example.demo.service

import com.example.demo.data.dto.PostCreateDto
import org.springframework.http.ResponseEntity

interface PostService {

    fun addPost(dto: PostCreateDto): ResponseEntity<*>

}