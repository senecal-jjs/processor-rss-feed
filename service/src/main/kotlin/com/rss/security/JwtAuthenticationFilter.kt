package com.rss.security

import com.rss.api.request.LoginRequest
import com.rss.data.json.OBJECT_MAPPER
import com.rss.data.exposed.Profile
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.lang.RuntimeException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val jwtBuilder: JwtBuilder,
    authenticationManager: AuthenticationManager
): UsernamePasswordAuthenticationFilter(authenticationManager) {
    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val credentials = OBJECT_MAPPER.readValue(request.inputStream, LoginRequest::class.java)

            Profile.getUserByUsername(credentials.username)?.let { profile ->
                return authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                        profile.username,
                        credentials.password,
                        profile.authorities
                    )
                )
            } ?: throw RuntimeException("Could not get user by username ${credentials.username}")

        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val token = jwtBuilder.generateToken(authResult)
        response.addHeader(jwtBuilder.jwtProperties.header, jwtBuilder.jwtProperties.tokenPrefix + token)
    }
}
