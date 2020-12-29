package security

import data.Profile
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class RssUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return Profile.getUserByUsername(username) ?: throw UsernameNotFoundException("Could not find username $username")
    }
}