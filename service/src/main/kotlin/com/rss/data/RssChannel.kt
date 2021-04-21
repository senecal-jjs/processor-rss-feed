package com.rss.data

import com.rss.api.response.RssChannelResponse
import com.rss.data.RssChannel.channelDesc
import com.rss.data.RssChannel.channelUrl
import com.rss.data.RssChannel.id
import com.rss.data.RssChannel.siteUrl
import com.rss.data.RssChannel.title
import com.rss.data.json.JsonBColumnType
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object RssChannel : UUIDTable("rss_channel") {
    val title = text("title")
    val siteUrl = text("site_url").nullable()
    val channelUrl = text("channel_url")
    val channelDesc = text("channel_desc")
    val imageUrl = text("image_url")
    val topicItem = RssChannel.registerColumn<TopicItem>("topics", object : JsonBColumnType<TopicItem>() {})

    fun getChannelIdByUrl(url: String): UUID? = transaction {
        RssChannel
            .select { channelUrl eq url }
            .firstOrNull()
            ?.let { it[id].value }
    }
}

class RssChannelRecord(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RssChannelRecord>(
        RssChannel
    )

    var title by RssChannel.title
    var siteUrl by RssChannel.siteUrl
    var channelUrl by RssChannel.channelUrl
    var channelDesc by RssChannel.channelDesc
    var imageUrl by RssChannel.imageUrl
    var topicItem by RssChannel.topicItem

    fun toResponse(): RssChannelResponse {
        return RssChannelResponse(
            id = id.value,
            title = title,
            siteUrl = siteUrl,
            channelUrl = channelUrl,
            description = channelDesc
        )
    }
}

data class TopicItem(
    val topics: List<String>
)