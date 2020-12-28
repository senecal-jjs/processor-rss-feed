package security

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    USER
}