package com.example.demo.controller

import com.example.demo.config.logger
import com.example.demo.data.dto.UserCreateDto
import com.example.demo.data.dto.UserUpdateDto
import com.example.demo.service.UserService
import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
@Slf4j
class UserController (
    private val userService: UserService,
){
    private val log = logger()

    @PostMapping("/save")
    fun saveUser(@RequestBody dto: UserCreateDto): ResponseEntity<*> {

        log.info{"[UserController::saveUser] try to save user! $dto"}
        return userService.saveUser(dto)
    }

    @GetMapping("/user-list")
    fun getUserList(): ResponseEntity<*>{
        log.info{"[UserController::getUserList] try to collect users!"}
        return userService.getUserList()
    }

    @PostMapping("/update")
    fun updateUser(@RequestBody dto: UserUpdateDto): ResponseEntity<*> {
        log.info{"[UserController::updateUser] try to update user! ${dto.uid}"}
        return userService.updateUser(dto)
    }

    @DeleteMapping("/delete")
    fun deleteUser(@RequestParam id: Long): ResponseEntity<*>{
        log.info{"[UserController::deleteUser] try to delete user! $id"}
        return userService.deleteUser(id)
    }

}