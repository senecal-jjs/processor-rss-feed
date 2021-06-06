package com.rss.data.exposed

import com.rss.data.domain.RssChannelRepository
import com.rss.data.model.RssChannel
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedRssChannelRepository: RssChannelRepository {
    override fun create(channelUrl: String) {
        TODO("Not yet implemented")
    }

    override fun fetchById(channelId: UUID): RssChannel? {
        return transaction {
            RssChannelRecord
                .findById(channelId)
                ?.let {
                    RssChannel(
                        id = it.id.value,
                        title = it.title,
                        channelUrl = it.channelUrl,
                        siteUrl = it.siteUrl,
                        description = it.channelDesc,
                        imageUrl = it.imageUrl
                    )
                }
        }
    }

    override fun fetchByUrl(url: String): RssChannel? {
        return RssChannelRecord
            .find { RssChannelTable.channelUrl eq url }
            .firstOrNull()
            ?.let {
                RssChannel(
                    id = it.id.value,
                    title = it.title,
                    channelUrl = it.channelUrl,
                    siteUrl = it.siteUrl,
                    description = it.channelDesc,
                    imageUrl = it.imageUrl
                )
            }
    }
}