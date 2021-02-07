package com.rss.data

import com.rss.data.json.JsonBColumnType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object RssChannel : Table("rss_channel") {
    val id = uuid("id").primaryKey()
    val title = text("title")
    val siteUrl = text("site_url").nullable()
    val channelUrl = text("channel_url")
    val channelDesc = text("channel_desc")
    val topics = RssChannel.registerColumn<Topics>("topics", object : JsonBColumnType<Topics>() {})

    fun getChannelIdByUrl(url: String): UUID? = transaction {
        RssChannel
            .select { channelUrl eq url }
            .firstOrNull()
            ?.let { it[id] }
    }

    fun getChannelById(id: UUID): List<ResultRow>

    fun getAllChannelUrlsAndId(): List<Pair<UUID, String>> = transaction {
        RssChannel
            .selectAll()
            .map { Pair(it[id], it[channelUrl]) }
    }

    fun save(
        inTitle: String,
        inSiteUrl: String,
        inChannelUrl: String,
        inChannelDesc: String,
        inTopics: Topics
    ): UUID {
        val channelId = UUID.randomUUID()

        transaction {
            RssChannel.insert {
                it[id] = channelId
                it[title] = inTitle
                it[siteUrl] = inSiteUrl
                it[channelUrl] = inChannelUrl
                it[channelDesc] = inChannelDesc
                it[topics] = inTopics
            }
        }

        return channelId
    }
}

data class Topics(
    val topics: List<String>
)