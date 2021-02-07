package com.rss.api.response

data class JwtAuthenticationResponse(
    val accessToken: String,
    val tokenType: String = "Bearer"
)