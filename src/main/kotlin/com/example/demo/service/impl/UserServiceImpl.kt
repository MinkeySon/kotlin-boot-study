package com.example.demo.service.impl

import com.example.demo.config.logger
import com.example.demo.data.domain.CommonResponse
import com.example.demo.data.domain.ResultCode
import com.example.demo.data.domain.User
import com.example.demo.data.dto.UserCreateDto
import com.example.demo.data.dto.UserDto
import com.example.demo.data.dto.UserUpdateDto
import com.example.demo.data.repository.UserRepository
import com.example.demo.service.UserService
import jakarta.transaction.Transactional
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {

    private val log = logger()

    @Transactional
    override fun saveUser(dto: UserCreateDto): ResponseEntity<*> {
        return try{
            val user = User.create(dto)
            userRepository.save(user)
            log.info{"[UserServiceImpl::saveUser] created successfully! $user"}

            CommonResponse.toResponseEntity(CommonResponse.success<Unit>())
        }catch(ex:Exception){
            log.error{"[UserServiceImpl::saveUser] creation failed! $ex"}

            CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.FAIL))
        }
    }

    override fun getUserList(): ResponseEntity<*> {
        return try{
            val userList: List<User> = userRepository.userList()
            log.info{"[UserServiceImpl::getUserList] collect user success !"}

            val dtoList: List<UserDto> = userList.map { o -> UserDto.toDto(o) }

            ResponseEntity.ok().body(dtoList)
        }catch(ex: Exception){
            log.error{"[UserSerivceImpl::getUserList] fail to get user list !"}
            CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.FAIL))
        }
    }

    @Transactional
    override fun updateUser(dto: UserUpdateDto): ResponseEntity<*> {
        return try{
            val user: User = userRepository.getById(dto.uid)
            log.info{"[UserServiceImpl::updateUser] try to update user ! ${dto.uid}"}

            user.update(dto)
            CommonResponse.toResponseEntity(CommonResponse.success<Unit>())
        } catch(ex: Exception){
            log.error{"[UserSerivceImpl::updateUser] fail to update user!"}
            CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.FAIL))
        }
    }

    @Transactional
    override fun deleteUser(id: Long): ResponseEntity<*> {
        return try{
            log.info{"[UserServiceImpl::deleteUser] try to delete user!"}
            userRepository.deleteById(id)
            log.info{"[UserServiceImpl::deleteUser] success to delete user!"}
            CommonResponse.toResponseEntity(CommonResponse.success<Unit>())
        } catch(ex: Exception){
            log.error { "[UserServiceImpl::deleteUser] fail to delete user! $ex]" }
            CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.FAIL))
        }
    }
}