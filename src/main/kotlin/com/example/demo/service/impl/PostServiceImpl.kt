package com.example.demo.service.impl

import com.example.demo.config.logger
import com.example.demo.data.domain.CommonResponse
import com.example.demo.data.domain.Post
import com.example.demo.data.domain.ResultCode
import com.example.demo.data.dto.PostCreateDto
import com.example.demo.data.repository.PostRepository
import com.example.demo.service.PostService
import jakarta.transaction.Transactional
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PostServiceImpl (
    val postRepository: PostRepository
): PostService {
    private val log = logger()

    @Transactional
    override fun addPost(dto: PostCreateDto): ResponseEntity<*> {
        log.info{"[PostServiceImpl::addPost] try to add post !"}

        return try{
            val post: Post = Post.create(dto)
            postRepository.save(post)

            CommonResponse.toResponseEntity(CommonResponse.success<Unit>())
        } catch (ex: Exception){
            log.error{"[PostServiceImpl::addPost] fail to error! $ex"}
            CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.FAIL))
        }
    }
}