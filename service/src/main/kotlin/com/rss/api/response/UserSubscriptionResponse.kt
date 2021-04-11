package com.rss.api.response

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
    val channelUrl: String,
    val siteUrl: String? = null,
    val description: String,
    val pubDate: OffsetDateTime? = null,
    val lastBuildDate: OffsetDateTime? = null,
    val imageUrl: String? = null,
    val items: List<RssItemResponse>? = null
)

data class RssItemResponse (
    val title: String,
    val itemUrl: String,
    val author: String,
    val description: String,
    val pubDate: OffsetDateTime,
    val content: String
)
