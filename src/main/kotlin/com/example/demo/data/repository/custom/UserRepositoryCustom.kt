package com.example.demo.data.repository.custom

import com.example.demo.data.domain.User
import java.util.Optional

interface UserRepositoryCustom {

    fun userList(): List<User>

    fun existsById(id: String): Boolean

    fun getByUserId(id: String): User?
}