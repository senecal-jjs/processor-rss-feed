package com.rss.data

import com.rss.model.Subscription as SubscriptionModel
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.ResultRow
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
            if (!isSubscribed(inProfileId, inChannelUrl)) {
                Subscription.insert {
                    it[id] = UUID.randomUUID()
                    it[profileId] = inProfileId
                    it[channelId] = inChannelId
                    it[channelUrl] = inChannelUrl
                    it[category] = inCategory
                }
            }
        }
    }

    fun getSubscriptions(
        inProfileId: UUID
    ): List<SubscriptionModel> = transaction {
        Subscription
            .select { profileId eq inProfileId }
            .toList()
            .map {
                SubscriptionModel(
                    id = it[id],
                    profileId = it[profileId],
                    channelId = it[channelId],
                    channelUrl = it[channelUrl],
                    category = it[category]
                )
            }
    }

    private fun isSubscribed(
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
