package com.rss.data.model

import java.time.OffsetDateTime
import java.util.*

data class RssChannel(
    val id: UUID,
    val title: String,
    val channelUrl: String,
    val siteUrl: String? = null,
    val description: String,
    val pubDate: OffsetDateTime? = null,
    val lastBuildDate: OffsetDateTime? = null,
    val imageUrl: String? = null,
    val items: List<RssItem>? = null
)