package config

import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "jwt")
@Validated
open class JWTProperties {
    @NotNull open lateinit var privateKey: String
    @NotNull open lateinit var publicKey: String
    @NotNull open lateinit var issuer: String
    @NotNull open lateinit var url: String
    @NotNull open lateinit var endpoint: String
    @NotNull open var expiresInMinutes: Int = 60
}