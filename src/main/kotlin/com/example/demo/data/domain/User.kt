package com.example.demo.data.domain

import com.example.demo.data.dto.UserCreateDto
import com.example.demo.data.dto.UserUpdateDto
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import lombok.ToString
import org.hibernate.annotations.Comment
import org.springframework.http.ResponseEntity

@Entity
@Table(name = "tb_user")
class User(

    @Column(nullable = false)
    @Comment("사용자 이름")
    var name: String,

    @Column
    @Comment("사용자 이메일")
    var email: String?,

    @Column(nullable = false)
    @Comment("사용자 id")
    val id: String? = "",

    @Column(nullable = false)
    @Comment("사용자 비밀번호")
    var password: String? = "",

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val posts: MutableList<Post> = mutableListOf()

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val uid: Long? = null

    fun update(dto: UserUpdateDto) {
        name = dto.name
        email = dto.email
    }

    companion object {
        fun create(dto: UserCreateDto): User = User(
            name = dto.name ?: "알 수 없음",
            email = dto.email,
            id = dto.id,
            password = dto.password
        )
    }

}