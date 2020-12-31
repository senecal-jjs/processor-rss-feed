package com.rss.security

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    USER {
        override fun getAuthority(): String {
            return "USER"
        }
    }
}