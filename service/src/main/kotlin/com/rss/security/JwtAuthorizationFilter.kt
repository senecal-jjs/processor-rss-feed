package com.rss.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(
    private val jwtBuilder: JwtBuilder,
    authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = request.getHeader(jwtBuilder.jwtProperties.header)

        if (header == null || !header.startsWith(jwtBuilder.jwtProperties.tokenPrefix)) {
            chain.doFilter(request, response)
            return
        }

        getAuthentication(request)?.run {
            SecurityContextHolder.getContext().authentication = this
        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        request.getHeader(jwtBuilder.jwtProperties.header)
            ?.replace(jwtBuilder.jwtProperties.tokenPrefix, "")
            ?.let { token ->
                if (jwtBuilder.isTokenValid(token)) {
                    val userId = jwtBuilder.getUserIdFromToken(token)
                    return UsernamePasswordAuthenticationToken(userId, null, emptyList())
                }
            }
        return null
    }
}