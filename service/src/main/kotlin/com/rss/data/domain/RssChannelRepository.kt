package com.rss.data.domain

import com.rss.data.model.RssChannel
import java.util.*

interface RssChannelRepository {
    fun create(channelUrl: String)

    fun fetchById(channelId: UUID): RssChannel?

    fun fetchByUrl(channelUrl: String): RssChannel?
}