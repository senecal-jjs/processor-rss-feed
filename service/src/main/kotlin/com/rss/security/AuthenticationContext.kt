package com.rss.security

import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

class Session {
    companion object {
        fun uuid(): UUID = SecurityContextHolder.getContext().authentication.principal as UUID
        fun uuidString(): String = uuid().toString()
    }
}