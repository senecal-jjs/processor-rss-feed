package com.rss.api

import java.time.OffsetDateTime

data class UserSubscriptionResponse(
    val feeds: List<FeedResponse>
)

data class FeedResponse(
    val category: String,
    val channels: List<RssChannelResponse>
)

data class RssChannelResponse(
    val title: String,
    val siteUrl: String,
    val description: String,
    val pubDate: OffsetDateTime,
    val lastBuildDate: OffsetDateTime,
    val imageUrl: String,
    val items: List<RssItemResponse>
)

data class RssItemResponse (
    val title: String,
    val itemUrl: String,
    val author: String,
    val description: String,
    val pubDate: OffsetDateTime,
    val content: String
)
