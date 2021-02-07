package com.rss.api.request

data class FeedSubscriptionRequest (
    val url: String,
    val category: String
)