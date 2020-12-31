package data

import data.json.JsonBColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import security.Role
import java.util.UUID

object Profile : Table("profile") {
    val id = uuid("id").primaryKey()
    val username = varchar("username", 30)
    val password = varchar("password", 30)
    val authority = Profile.registerColumn<Authority>("authority", object : JsonBColumnType<Authority>() {})

    fun getUserByUsername(inUsername: String): model.Profile? = transaction {
        Profile
            .select { username eq inUsername }
            .firstOrNull()
            ?.let {
                model.Profile(
                    id = it[id],
                    username = it[username],
                    password = it[password],
                    authorities = it[authority].roles
                )
            }
    }

    fun getUserById(userId: UUID): model.Profile? = transaction {
        Profile
            .select { id eq userId }
            .firstOrNull()
            ?.let {
                model.Profile(
                    id = it[Profile.id],
                    username = it[username],
                    password = it[password],
                    authorities = it[authority].roles
                )
            }
    }

    fun usernameExists(inUsername: String): Boolean = transaction {
        Profile
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
            Profile.insert {
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
