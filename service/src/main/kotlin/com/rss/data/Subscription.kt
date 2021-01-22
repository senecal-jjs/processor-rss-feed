package com.rss.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Subscription : Table("subscription") {
    val id = uuid("id")
    val profileId = uuid("profile_id")
    val channelId = uuid("channel_id")
    val channelUrl = text("channel_url")
    val category = text("category")

    fun subscribe(
        inProfileId: UUID,
        inChannelId: UUID,
        inChannelUrl: String,
        inCategory: String
    ) {
        transaction {
            Subscription.insert {
                it[id] = UUID.randomUUID()
                it[profileId] = inProfileId
                it[channelId] = inChannelId
                it[channelUrl] = inChannelUrl
                it[category] = inCategory
            }
        }
    }

    fun isSubscribed(
        inProfileId: UUID,
        inChannelUrl: String
    ): Boolean {
        return Subscription
            .select { profileId eq inProfileId }
            .andWhere { channelUrl eq inChannelUrl }
            .singleOrNull()
            ?.let { true } ?: false
    }
}