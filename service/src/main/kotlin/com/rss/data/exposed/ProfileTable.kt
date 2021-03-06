package com.rss.data.exposed

import com.rss.data.json.JsonBColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import com.rss.security.Role
import java.util.UUID

object ProfileTable : Table("profile") {
    val id = uuid("id").primaryKey()
    val username = varchar("username", 50)
    val password = varchar("password", 300)
    val authority = ProfileTable.registerColumn<Authority>("authority", object : JsonBColumnType<Authority>() {})

    fun getUserByUsername(inUsername: String): com.rss.data.model.Profile? = transaction {
        ProfileTable
            .select { username eq inUsername }
            .firstOrNull()
            ?.let {
                com.rss.data.model.Profile(
                    id = it[id],
                    username = it[username],
                    password = it[password],
                    authorities = it[authority].roles
                )
            }
    }

    fun getUserById(userId: UUID): com.rss.data.model.Profile? = transaction {
        ProfileTable
            .select { id eq userId }
            .firstOrNull()
            ?.let {
                com.rss.data.model.Profile(
                    id = it[id],
                    username = it[username],
                    password = it[password],
                    authorities = it[authority].roles
                )
            }
    }

    fun usernameExists(inUsername: String): Boolean = transaction {
        ProfileTable
            .select { username eq inUsername }
            .firstOrNull()
            ?.let { true } ?: false
    }

    fun saveUser(
        inUsername: String,
        inPassword: String,
        roles: MutableList<Role> = mutableListOf(Role.USER)
    ) {
        transaction {
            ProfileTable.insert {
                it[id] = UUID.randomUUID()
                it[username] = inUsername
                it[password] = inPassword
                it[authority] = Authority(roles = roles)
            }
        }
    }
}

data class Authority(
    val roles: MutableList<Role>
)
