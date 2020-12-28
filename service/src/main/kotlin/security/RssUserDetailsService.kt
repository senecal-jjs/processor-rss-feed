package security

import data.Profile
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class RssUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        Profile.getUserByUsername(username)?.let {
            UserDetails(

            )
        }
    }
}