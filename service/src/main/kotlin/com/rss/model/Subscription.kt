package com.rss.model

import java.util.*

data class Subscription(
    val id: UUID,
    val profileId: UUID,
    val channelId: UUID,
    val channelUrl: String,
    val category: String
)