package com.rss.security

import com.rss.data.exposed.Profile
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RssUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return Profile.getUserByUsername(username) ?: throw UsernameNotFoundException("Could not find username $username")
    }

    fun loadUserById(id: UUID): UserDetails {
        return Profile.getUserById(id) ?: throw UsernameNotFoundException("Could not find id $id")
    }
}