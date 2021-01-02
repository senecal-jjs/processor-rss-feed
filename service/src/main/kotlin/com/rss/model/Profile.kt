package com.rss.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import com.rss.security.Role
import java.util.UUID

data class Profile(
    private val id: UUID,
    private val username: String,
    private val password: String,
    private val authorities: MutableList<Role>
) : UserDetails {
    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    fun getId(): UUID {
        return id
    }
}
