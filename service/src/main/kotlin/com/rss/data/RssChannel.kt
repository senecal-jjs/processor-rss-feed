package com.rss.data

import com.rss.data.json.JsonBColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object RssChannel : Table("rss_channel") {
    val id = uuid("id").primaryKey()
    val title = text("title")
    val siteUrl = text("site_url").nullable()
    val channelUrl = text("channel_url")
    val channelDesc = text("channel_desc")
    val topics = RssChannel.registerColumn<Topics>("topics", object : JsonBColumnType<Topics>() {})

    fun isChannelSaved(url: String): Boolean = transaction {
        RssChannel
            .select { channelUrl eq url }
            .firstOrNull()
            ?.let { true } ?: false
    }

    fun getChannelIdByUrl(url: String): UUID? = transaction {
        RssChannel
            .select { channelUrl eq url }
            .firstOrNull()
            ?.let { it[id] }
    }

    fun save(
        inTitle: String,
        inSiteUrl: String,
        inChannelUrl: String,
        inChannelDesc: String,
        topics: Topics
    ) {
        transaction {

        }
    }
}

data class Topics(
    val topics: List<String>
)