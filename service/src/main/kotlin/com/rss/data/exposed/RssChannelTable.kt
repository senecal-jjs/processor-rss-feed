package com.rss.data.exposed

import com.rss.api.response.RssChannelResponse
import com.rss.data.json.JsonBColumnType
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object RssChannelTable : UUIDTable("rss_channel") {
    val title = text("title")
    val siteUrl = text("site_url").nullable()
    val channelUrl = text("channel_url")
    val channelDesc = text("channel_desc")
    val imageUrl = text("image_url")
    val topicItem = RssChannelTable.registerColumn<TopicItem>("topics", object : JsonBColumnType<TopicItem>() {})

    fun getChannelIdByUrl(url: String): UUID? = transaction {
        RssChannelTable
            .select { channelUrl eq url }
            .firstOrNull()
            ?.let { it[id].value }
    }
}

class RssChannelRecord(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RssChannelRecord>(RssChannelTable)

    var title by RssChannelTable.title
    var siteUrl by RssChannelTable.siteUrl
    var channelUrl by RssChannelTable.channelUrl
    var channelDesc by RssChannelTable.channelDesc
    var imageUrl by RssChannelTable.imageUrl
    var topicItem by RssChannelTable.topicItem

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