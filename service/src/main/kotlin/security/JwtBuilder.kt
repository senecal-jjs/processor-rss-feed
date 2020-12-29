package security

import config.JWTProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties(JWTProperties::class)
class JwtBuilder(jwtProperties: JWTProperties) {
    private val logger = LoggerFactory.getLogger(JwtBuilder::class.java)


}