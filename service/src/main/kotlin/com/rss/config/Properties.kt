package com.rss.config

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.security.Key

@ConfigurationProperties(prefix = "jwt")
@Validated
open class JWTProperties {
    @NotNull open var privateKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    @NotNull open lateinit var publicKey: String
    @NotNull open lateinit var issuer: String
    @NotNull open lateinit var url: String
    @NotNull open lateinit var endpoint: String
    @NotNull open var expiresInMinutes: Int = 60
}

@ConfigurationProperties(prefix = "database")
@Validated
open class DatabaseProperties {
    @NotNull
    open lateinit var name: String
    @NotNull
    open lateinit var username: String
    @NotNull
    open lateinit var password: String
    @NotNull
    open lateinit var hostname: String
    @NotNull
    open lateinit var port: Integer
    @NotNull
    open lateinit var schema: String
    @NotNull
    open lateinit var connectionPoolSize: String
}