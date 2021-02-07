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
import java.util.UUID

//object AggregateStore : UUIDTable("crm_aggregate") {
//    val aggregation: Column<AggRecord> = proto("agg", AggRecord.getDefaultInstance())
//    val retry: Column<Boolean> = bool("retry")
//}
//
//class AggregateRecord(id: EntityID<UUID>) : UUIDEntity(id) {
//    companion object : UUIDEntityClass<AggregateRecord>(
//        AggregateStore
//    )
//
//    var aggregation by AggregateStore.aggregation
//    var retry by AggregateStore.retry
//}

object RssChannel : UUIDTable("rss_channel") {
    val title = text("title")
    val siteUrl = text("site_url").nullable()
    val channelUrl = text("channel_url")
    val channelDesc = text("channel_desc")
    val topics = RssChannel.registerColumn<Topics>("topics", object : JsonBColumnType<Topics>() {})
}

class RssChannelRecord(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RssChannelRecord>(
        RssChannel
    )

    var title by RssChannel.title
    var siteUrl by RssChannel.siteUrl
    var channelUrl by RssChannel.channelUrl
    var channelDesc by RssChannel.channelDesc
    var topics by RssChannel.topics

    fun toResponse(): RssChannelResponse {
        return RssChannelResponse(
            title = title,
            siteUrl = siteUrl,
            description = channelDesc
        )
    }
}

/**
 *     fun getChannelIdByUrl(url: String): UUID? = transaction {
RssChannel
.select { channelUrl eq url }
.firstOrNull()
?.let { it[id] }
}

fun getChannelById(channelId: UUID): ResultRow? = transaction {
RssChannel.select { id eq channelId }.firstOrNull()
}

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
 */

data class Topics(
    val topics: List<String>
)