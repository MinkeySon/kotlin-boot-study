package com.example.demo.data.domain

import com.example.demo.data.dto.UserCreateDto
import com.example.demo.data.dto.UserUpdateDto
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serial

@Entity
@Table(name = "tb_user")
class User(

    @Column(nullable = false)
    @Comment("사용자 이름")
    var name: String,

    @Column
    @Comment("사용자 이메일")
    var email: String? =null,

    @Column(nullable = false, unique = true)
    @Comment("사용자 id")
    val id: String,

    @Column(nullable = false)
    @Comment("사용자 비밀번호")
    val pwd: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val posts: MutableList<Post> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    private val roles: List<String> = ArrayList(),

) : BaseTimeEntity(), UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val uid: Long? = null

    fun update(dto: UserUpdateDto) {
        name = dto.name
        email = dto.email
    }

    override fun getAuthorities(): Collection<out GrantedAuthority> {
       return roles.map{SimpleGrantedAuthority(it)}
    }

    override fun getPassword(): String? =password

    override fun getUsername(): String =id

    companion object {

        @Serial
        private const val serialVersionUID: Long = 937195358763181265L

        fun create(dto: UserCreateDto): User = User(
            name = dto.name ?: "알 수 없음",
            email = dto.email,
            id = dto.id,
            pwd = dto.pwd,
            roles = listOf("ROLE_USER"),
        )
    }
}