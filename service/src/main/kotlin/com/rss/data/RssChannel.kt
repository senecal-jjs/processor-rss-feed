package com.rss.data

import com.rss.data.json.JsonBColumnType
import org.jetbrains.exposed.sql.Table

object RssChannel : Table("rss_channel") {
    val id = uuid("id").primaryKey()
    val title = text("title")
    val siteUrl = text("site_url").nullable()
    val channelUrl = text("channel_url")
    val channelDesc = text("channel_desc")
    val topics = RssChannel.registerColumn<Topics>("topics", object : JsonBColumnType<Topics>() {})


}

data class Topics(
    val topics: List<String>
)