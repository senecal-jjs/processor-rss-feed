package com.rss.api

import java.time.OffsetDateTime

data class RssChannelResponse (
    val title: String,
    val siteUrl: String,
    val description: String,
    val pubDate: OffsetDateTime,
    val lastBuildDate: OffsetDateTime,
    val imageUrl: String,
    val items: List<RssItem>
)

data class RssItem (
    val title: String,
    val itemUrl: String,
    val author: String,
    val description: String,
    val pubDate: OffsetDateTime,
    val content: String
)