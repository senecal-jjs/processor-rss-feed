package com.rss.api

data class FeedSubscriptionRequest (
    val channelUrl: String,
    val category: String
)