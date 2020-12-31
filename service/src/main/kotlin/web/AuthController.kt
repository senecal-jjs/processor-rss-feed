package web

import api.JwtAuthenticationResponse
import api.LoginRequest
import api.SignUpRequest
import data.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import security.JwtBuilder
import kotlin.math.sign

@RestController
@RequestMapping("api/v1/auth")
class AuthController(
    private val passwordEncoder: PasswordEncoder,
    private val jwtBuilder: JwtBuilder,
    private val authenticationManager: AuthenticationManager
) {
    @PostMapping("/sign-in")
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

    @PostMapping("/sign-up")
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