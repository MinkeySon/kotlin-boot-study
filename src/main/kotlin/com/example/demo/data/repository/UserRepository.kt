package com.example.demo.data.repository

import com.example.demo.data.domain.User
import com.example.demo.data.repository.custom.UserRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom {
}