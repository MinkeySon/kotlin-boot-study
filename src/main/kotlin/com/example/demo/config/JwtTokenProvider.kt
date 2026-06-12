package com.example.demo.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider (
    private val userDetailsService: UserDetailsService,
    @Value("\${jwt.secret}")
    private val key: String
){
    private val signingKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key))
    private val log = logger()
    private val accessTokenValidTime : Long = 1000L * 60 * 60
    private val refreshTokenValidTime : Long = 1000L * 60 * 60 * 24 * 14

    fun createAccessToken(id:String, roles:List<String>): String{
        val token: String = createToken(id, roles, accessTokenValidTime)
        return token
    }

    fun createToken(id:String, roles:List<String>, validTime:Long): String{
        log.info{"[JwtTokenProvider::createToken] try to create token ! $id"}
        val now : Date = Date()
        val signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key))

        return Jwts.builder()
            .subject(id)
            .claim("roles", roles)
            .issuedAt(now)
            .expiration(Date(now.time + validTime))
            .signWith(signingKey)
            .compact()
            .also { log.info { "[JwtTokenProvider::createToken] create token success" } }
    }

    fun getAuthentication(token:String): Authentication {
        log.info {"[JwtTokenProvider::getAuthentication] start to check authentication"}
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(getUserId(token))
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    fun getUserId(token: String): String {
        log.info { "[JwtTokenProvider::getUserId] start to decode token!" }

        val info: String = Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject

        log.info { "[JwtTokenProvider::getUserId] decode success!" }
        return info
    }

    fun refreshToken(token:String, id:String): String {
        log.info { "[JwtTokenProvider::refreshToken] start to refresh token!" }

        if (validateToken(token)){
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(id)
            val roles: List<String> = userDetails.authorities.map{authority -> authority.toString()}
            return createAccessToken(id, roles)
        }else{
            throw RuntimeException("refresh token is not valid").also { log.error(it.toString()) }
        }
    }

    fun extractToken(request: HttpServletRequest):String{
        log.info {"[JwtTokenProvider::extractToken] start to check authentication"}
        return request.getHeader("Authorization").also{ log.info { "[JwtTokenProvider::extractToken] founded!" } }
    }

    fun validateToken(token:String): Boolean {
        log.warn {"[JwtTokenProvider::validateToken] start to check token's validation!"}

        return try {
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token)
            true
        } catch (ex: JwtException) {
            log.info { "[JwtTokenProvider::validateToken] token is invalid" }
            false
        } catch (ex: IllegalArgumentException) {
            log.info { "[JwtTokenProvider::validateToken] token is empty or null" }
            false
        }
    }
}