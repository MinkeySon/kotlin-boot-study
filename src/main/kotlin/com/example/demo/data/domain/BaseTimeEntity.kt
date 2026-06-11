package com.example.demo.data.domain

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import lombok.Getter
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@Getter
abstract class BaseTimeEntity(

    @CreatedDate
    @Comment("데이터 생성 시간")
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Comment("데이터 수정 시간")
    var updatedAt: LocalDateTime? = null,
) {
    @PrePersist
    fun prePersist(){
        this.createdAt = LocalDateTime.now()
        this.updatedAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate(){
        this.updatedAt = LocalDateTime.now()
    }
}