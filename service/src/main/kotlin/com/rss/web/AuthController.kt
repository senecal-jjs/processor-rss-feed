package com.rss.web

import com.rss.api.JwtAuthenticationResponse
import com.rss.api.LoginRequest
import com.rss.api.SignUpRequest
import com.rss.data.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import com.rss.security.JwtBuilder
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val passwordEncoder: PasswordEncoder,
    private val jwtBuilder: JwtBuilder,
    private val authenticationManager: AuthenticationManager
) {
    @GetMapping("/test")
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("test success")
    }

    @PostMapping("/login")
    fun authenticateUser(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<JwtAuthenticationResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        return jwtBuilder.generateToken(authentication).let {
            ResponseEntity.ok(JwtAuthenticationResponse(accessToken = it))
        }
    }

    @PostMapping("/register")
    fun registerUser(
        @RequestBody signUpRequest: SignUpRequest
    ): ResponseEntity<String> {
        if (Profile.usernameExists(signUpRequest.username)) {
            return ResponseEntity("Username is already taken", HttpStatus.BAD_REQUEST)
        }

        Profile.saveUser(
            inUsername = signUpRequest.username,
            inPassword = passwordEncoder.encode(signUpRequest.password)
        )

        return ResponseEntity.ok("${signUpRequest.username} created")
    }
}