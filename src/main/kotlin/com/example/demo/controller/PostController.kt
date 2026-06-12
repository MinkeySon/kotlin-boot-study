package com.example.demo.controller

import com.example.demo.config.logger
import com.example.demo.data.dto.PostCreateDto
import com.example.demo.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/post")
class PostController (
    val postService: PostService
){
    private val log = logger()

    @PostMapping("/add")
    fun addPost(@RequestBody dto: PostCreateDto): ResponseEntity<*> {
        log.info{"[PostController::addPost] try to add post! $dto"}
        return postService.addPost(dto)
    }
}