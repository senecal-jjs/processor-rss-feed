package com.rss.security

import com.rss.config.JWTProperties
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import com.rss.model.Profile
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
@EnableConfigurationProperties(JWTProperties::class)
class JwtBuilder(private val jwtProperties: JWTProperties) {
    private val logger = LoggerFactory.getLogger(JwtBuilder::class.java)

    fun generateToken(authentication: Authentication): String {
        val userPrincipal: Profile = authentication.principal as Profile
        val expiryDate = Date(Date().time + (jwtProperties.expiresInMinutes * 60 * 60))

        return Jwts.builder()
            .setSubject(userPrincipal.getId().toString())
            .claim("USERNAME", userPrincipal.username)
            .claim("ROLES", userPrincipal.authorities)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtProperties.privateKey)
            .compact()
    }

    fun getUserIdFromToken(token: String): UUID {
        return Jwts.parser()
            .setSigningKey(jwtProperties.privateKey)
            .parseClaimsJws(token)
            .body
            .let {
                UUID.fromString(it.subject)
            }
    }

    fun isTokenValid(token: String): Boolean {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.privateKey)
                .parseClaimsJws(token)

            return true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature");
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token");
        } catch (e: ExpiredJwtException) {
            logger.error("Expired JWT token");
        } catch (e: UnsupportedJwtException) {
            logger.error("Unsupported JWT token");
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty.");
        }
        return false
    }
}
