package data

import data.json.JsonBColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import security.Role

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
                    username = it[username],
                    password = it[password],
                    authorities = it[authority].roles
                )
            }
    }

    fun saveUser(inUsername: String, inPassword: String) {
        transaction {
            Profile.insert {
                it[username] = inUsername
                it[password] = inPassword
            }
        }
    }
}

data class Authority(
    val roles: MutableList<Role>
)
