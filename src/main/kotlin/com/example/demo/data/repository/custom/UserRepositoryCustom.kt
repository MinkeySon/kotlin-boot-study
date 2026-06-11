package com.example.demo.data.repository.custom

import com.example.demo.data.domain.User
import java.util.Optional

interface UserRepositoryCustom {

    fun userList(): Optional<List<User>>

}