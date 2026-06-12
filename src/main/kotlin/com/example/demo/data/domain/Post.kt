package com.example.demo.data.domain

import com.example.demo.data.dto.PostCreateDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "tb_post")
class Post (
    @Column(nullable = false)
    @Comment("제목")
    var title: String,

    @Column(nullable = false)
    @Comment("내용")
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    var user: User,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var pid: Long? = null

    companion object {
        fun create(dto: PostCreateDto): Post = Post(title=dto.title, content=dto.content, user=dto.user)
    }
}