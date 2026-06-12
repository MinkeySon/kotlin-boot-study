package com.example.demo.service.impl

import com.example.demo.data.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(id: String): UserDetails =
        userRepository.getByUserId(id)
            ?: throw UsernameNotFoundException("user not found: $id")
}