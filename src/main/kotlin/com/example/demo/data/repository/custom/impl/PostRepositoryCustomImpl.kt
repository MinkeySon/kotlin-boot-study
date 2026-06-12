package com.example.demo.data.repository.custom.impl

import com.example.demo.data.repository.custom.PostRepositoryCustom
import com.example.demo.data.repository.custom.UserRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryCustomImpl (
    private val jpaQueryFactory: JPAQueryFactory
) : PostRepositoryCustom {
}