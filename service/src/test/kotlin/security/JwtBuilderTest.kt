package security

import com.rss.config.JWTProperties
import com.rss.security.JwtBuilder
import org.junit.jupiter.api.Test

class JwtBuilderTest {
    private val jwtProperties: JWTProperties = JWTProperties()
    private val jwtBuilder = JwtBuilder(jwtProperties)

    @Test
    fun `jwt validates properly`() {
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjYzJmMjFjOC02NWYxLTQxOTItYjI3Yi0yZmM5MTBjNDYyYTMiLCJVU0VSTkFNRSI6InRlc3RAeWFob28uY29tIiwiUk9MRVMiOlsiVVNFUiJdLCJpYXQiOjE2MDk2MDc2NDQsImV4cCI6MTYwOTYwNzg2MH0.mWPWkfj_zLbufbCxPtN4AR17MlekvBF1H7H6lMpWtuIzfyHd_czOz9fSnPVmcLShT0Q1NI_nWuVxVDshzrP6vg"
        jwtBuilder.isTokenValid(token)
    }
}