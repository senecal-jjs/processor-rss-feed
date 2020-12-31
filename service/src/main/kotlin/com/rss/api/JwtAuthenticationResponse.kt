package com.rss.api

data class JwtAuthenticationResponse(
    val accessToken: String,
    val tokenType: String = "Bearer"
)