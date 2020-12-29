package security

import config.JWTProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import model.Profile
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
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtProperties.privateKey)
            .compact()
    }

}