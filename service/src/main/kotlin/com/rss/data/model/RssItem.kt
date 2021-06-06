package com.rss.data.model

import java.time.OffsetDateTime

data class RssItem(
    val title: String,
    val itemUrl: String,
    val author: String,
    val description: String,
    val pubDate: OffsetDateTime,
    val content: String?
)