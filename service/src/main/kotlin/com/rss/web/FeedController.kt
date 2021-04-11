package com.rss.web

import com.rss.api.response.FeedResponse
import com.rss.api.request.FeedSubscriptionRequest
import com.rss.api.response.RssChannelResponse
import com.rss.api.response.RssItemResponse
import com.rss.api.response.UserSubscriptionResponse
import com.rss.data.RssChannel
import com.rss.data.RssChannelRecord
import com.rss.data.Subscription
import com.rss.data.TopicItem
import com.rss.extension.toOffsetDateTime
import com.rss.security.Session
import com.rss.service.RssReaderService
import com.rss.service.RssSearchService
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/feeds")
class FeedController(
    private val rssSearchService: RssSearchService,
    private val rssReaderService: RssReaderService
) {
    @GetMapping("/search-feeds")
    fun searchFeeds(
        @RequestParam searchTerm: String
    ): List<RssChannelResponse> {
        return rssSearchService.fuzzySearch(searchTerm)
    }

    @PostMapping("/register-feed")
    fun registerFeed(
        @RequestBody feedSubscriptionRequest: FeedSubscriptionRequest
    ) {
        // save new feed if it doesn't exist
        val channelId = RssChannel.getChannelIdByUrl(feedSubscriptionRequest.url) ?: let {
            rssReaderService.saveFeed(
                feedSubscriptionRequest.url,
                TopicItem(topics = listOf(feedSubscriptionRequest.category))
            )
        }

        Subscription.subscribe(
            Session.uuid(),
            channelId,
            feedSubscriptionRequest.url,
            feedSubscriptionRequest.category
        )
    }

    @GetMapping("/get-feeds")
    fun getFeeds(): UserSubscriptionResponse {
        val subs = Subscription.getSubscriptions(Session.uuid())
        val categories = subs.map { it.category }.distinct()
        val feeds = categories.map { currentCategory ->
            FeedResponse(
                category = currentCategory,
                channels = subs
                    .filter { it.category == currentCategory }
                    .map {
                        rssReaderService.getFeed(it.channelUrl).let { syndFeed ->
                            RssChannelResponse(
                                title = syndFeed.title,
                                siteUrl = syndFeed.link,
                                channelUrl = it.channelUrl,
                                description = syndFeed.description,
                                pubDate = syndFeed.publishedDate.toOffsetDateTime(),
                                lastBuildDate = syndFeed.publishedDate.toOffsetDateTime(),
                                imageUrl = syndFeed.image.url,
                                items = syndFeed.entries.map { entry ->
                                    RssItemResponse(
                                        title = entry.title,
                                        itemUrl = entry.link,
                                        author = entry.author,
                                        description = entry.description.value,
                                        pubDate = entry.publishedDate.toOffsetDateTime(),
                                        content = entry.contents.first().value
                                    )
                                }
                            )
                        }
                    }
            )
        }

        return UserSubscriptionResponse(feeds = feeds)
    }
}