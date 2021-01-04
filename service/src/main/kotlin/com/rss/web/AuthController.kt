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
@RequestMapping("/api/v1/auth")
class AuthController(
    private val passwordEncoder: PasswordEncoder,
) {
    @GetMapping("/test")
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("test success")
    }

    @GetMapping("/isAuthenticated")
    fun isAuthenticated(): ResponseEntity<String> {
        return ResponseEntity.ok("User authenticated")
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