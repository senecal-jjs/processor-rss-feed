package api

data class LoginRequest(
    val username: String,
    val password: String
)