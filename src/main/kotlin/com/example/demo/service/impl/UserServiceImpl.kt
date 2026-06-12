package com.example.demo.service.impl

import com.example.demo.config.JwtTokenProvider
import com.example.demo.config.logger
import com.example.demo.data.domain.CommonResponse
import com.example.demo.data.domain.ResultCode
import com.example.demo.data.domain.User
import com.example.demo.data.dto.SignInDto
import com.example.demo.data.dto.UserCreateDto
import com.example.demo.data.dto.UserDto
import com.example.demo.data.dto.UserUpdateDto
import com.example.demo.data.repository.UserRepository
import com.example.demo.service.UserService
import jakarta.transaction.Transactional
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: StringRedisTemplate,

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

    @Transactional
    override fun signUp(dto: UserCreateDto): ResponseEntity<*> {
        return try{
            log.info{"[UserServiceImpl::signUp] try to sign up!"}
            val isUserPresent: Boolean = userRepository.existsById(dto.id)

            if (isUserPresent){
                log.warn{"[UserServiceImpl::signUp] user id is already exists!"}
                return CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.BAD_REQUEST))
            }

            val newUser: User = User.create(
                dto.copy(pwd = requireNotNull(passwordEncoder.encode(dto.pwd)))
            )

            val result:User = userRepository.save(newUser)
            val token = jwtTokenProvider.createAccessToken(result.id, result.authorities.mapNotNull { it.authority })
            CommonResponse.toResponseEntity(CommonResponse.success(token))

        } catch (ex: Exception){
            log.error { "[UserServiceImpl::signUp] fail to sign up! $ex" }
            CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.FAIL))
        }
    }

    override fun signIn(dto: SignInDto): ResponseEntity<*> {
        val foundedUser: User = userRepository.getByUserId(dto.id)?: return CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.BAD_REQUEST))

        val pwd: String = foundedUser.pwd

        if(!passwordEncoder.matches(dto.pwd, pwd)){
            return CommonResponse.toResponseEntity(CommonResponse.fail<Unit>(ResultCode.BAD_REQUEST))
        }

        val token = jwtTokenProvider.createAccessToken(foundedUser.id, foundedUser.authorities.mapNotNull { it.authority })

        return CommonResponse.toResponseEntity(CommonResponse.success(token))
    }

    override fun logOut(token: String): ResponseEntity<*> {
        val remaining = jwtTokenProvider.getRemainingValidity(token)
        if (remaining > 0) {
            redisTemplate.opsForValue().set("blacklist:$token", "logout", Duration.ofMillis(remaining))
        }
        return CommonResponse.toResponseEntity(CommonResponse.success<Unit>())
    }

    private fun isIdExist(id: String): Boolean{
        return userRepository.existsById(id)
    }
}