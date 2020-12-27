package data

import org.jetbrains.exposed.sql.Table

object Profile : Table("profile") {
    val id = uuid("id")
    val username = varchar("username", 30)
    val password = varchar("password", 30)
}
