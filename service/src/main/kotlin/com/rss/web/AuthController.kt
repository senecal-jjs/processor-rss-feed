package com.rss.web

import com.rss.api.request.SignUpRequest
import com.rss.data.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import com.rss.security.JwtBuilder
import com.rss.security.Role
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val passwordEncoder: PasswordEncoder,
    private val jwtBuilder: JwtBuilder,
    private val authenticationManager: AuthenticationManager
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
        @RequestBody signUpRequest: SignUpRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        if (Profile.usernameExists(signUpRequest.username)) {
            return ResponseEntity("Username is already taken", HttpStatus.BAD_REQUEST)
        }

        Profile.saveUser(
            inUsername = signUpRequest.username,
            inPassword = passwordEncoder.encode(signUpRequest.password)
        )

        val authResult = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                signUpRequest.username,
                signUpRequest.password,
                listOf(Role.USER)
            )
        )

        SecurityContextHolder.getContext().authentication = authResult;

        val token = jwtBuilder.generateToken(authResult)
        response.addHeader(jwtBuilder.jwtProperties.header, jwtBuilder.jwtProperties.tokenPrefix + token)

        return ResponseEntity.ok("${signUpRequest.username} created")
    }
}