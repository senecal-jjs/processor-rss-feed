package api

data class JwtAuthenticationResponse(
    val accessToken: String,
    val tokenType: String = "Bearer"
)