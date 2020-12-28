package data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Profile : Table("profile") {
    val id = uuid("id")
    val username = varchar("username", 30)
    val password = varchar("password", 30)

    fun getUserByUsername(inUsername: String): model.Profile? = transaction {
        Profile
            .select { username eq inUsername }
            .firstOrNull()
            ?.let {
                model.Profile(
                    username = it[username],
                    password = it[password]
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
