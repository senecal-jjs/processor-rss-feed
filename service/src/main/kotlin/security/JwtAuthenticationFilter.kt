package security

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.Exception

class JwtAuthenticationFilter(
    private val jwtBuilder: JwtBuilder,
    private val rssUserDetailsService: RssUserDetailsService
): OncePerRequestFilter() {
    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            getJwtFromRequest(request)
                ?.also { token ->
                    if (StringUtils.hasText(token) && jwtBuilder.isTokenValid(token)) {
                        jwtBuilder.getUserIdFromToken(token).run {
                            val profile = rssUserDetailsService.loadUserById(this)
                            val authentication = UsernamePasswordAuthenticationToken(profile, null, profile.authorities)
                            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                            SecurityContextHolder.getContext().authentication = authentication
                        }
                    }
                }
        } catch (e: Exception) {
            logger.error("Could not set user auth in security context, $e")
        }
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        request
            .getHeader("Authorization")
            .let { token ->
                return if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                    token.substring(7, token.length)
                } else {
                    null
                }
            }
    }
}
