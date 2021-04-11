package util

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object DbUtils {
    fun <T : Table, R> connect(table: T, logicWhileConnected: () -> R): R {
        return connect(listOf(table), logicWhileConnected)
    }

    fun <T : Table, R> connect(tables: List<T>, logicWhileConnected: () -> R): R {
        Database.connect(
            url = listOf(
                "jdbc:h2:mem:test",
                "LOCK_TIMEOUT=10000",
                "INIT=" + listOf(
                    "create domain if not exists jsonb as other",
                    "create domain if not exists TIMESTAMPTZ as TIMESTAMP WITH TIME ZONE"
                ).joinToString("\\;")
            ).joinToString(";") + ";",
            driver = "org.h2.Driver"
        )
        return transaction {
            tables.forEach { SchemaUtils.create(it) }

            logicWhileConnected.invoke()
        }
    }
}