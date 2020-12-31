package com.rss.data.json

import org.jetbrains.exposed.sql.ColumnType
import org.postgresql.util.PGobject
import java.lang.reflect.ParameterizedType
import java.sql.PreparedStatement

abstract class JsonBColumnType<T: Any>: ColumnType() {
    @Suppress("UNCHECKED_CAST")
    var clazz: Class<T> = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>

    override fun sqlType() = "JSONB"

    override fun setParameter(stmt: PreparedStatement, index: Int, value: Any?) {
        value?.let { OBJECT_MAPPER.writeValueAsString(it).psqlEscape() }
            .let {
                PGobject().apply {
                    type = "jsonb"
                    this.value = it
                }
            }.run {
                stmt.setObject(index, this)
            }
    }

    override fun valueFromDB(value: Any): T {
        if (value is PGobject) {
            val json = value.value
            return OBJECT_MAPPER.readValue(json, clazz)
        }
        return value as T
    }
}

fun String.psqlEscape(): String {
    /* find single quotes that are NOT preceded or followed by other single quotes
    * ?<! = negative look behind
    * ?! = negative look ahead */
    return replace(Regex("(?<!')(')(?!')"), "''")
}