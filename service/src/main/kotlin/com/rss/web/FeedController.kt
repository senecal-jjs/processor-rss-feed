package com.rss.web

import com.rss.api.response.FeedResponse
import com.rss.api.request.FeedSubscriptionRequest
import com.rss.api.response.FeedSearchResponse
import com.rss.api.response.RssChannelResponse
import com.rss.api.response.UserSubscriptionResponse
import com.rss.data.RssChannel
import com.rss.data.RssChannelRecord
import com.rss.data.Subscription
import com.rss.data.TopicItem
import com.rss.extension.toRssChannelResponse
import com.rss.security.Session
import com.rss.service.RssReaderService
import com.rss.service.RssSearchService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/feeds")
class FeedController(
    private val rssSearchService: RssSearchService,
    private val rssReaderService: RssReaderService
) {
    @GetMapping("/search-feeds")
    fun searchFeeds(
        @RequestParam searchTerm: String
    ): FeedSearchResponse {
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
                        rssReaderService
                            .getFeed(it.channelUrl)
                            .toRssChannelResponse(it.channelId, it.channelUrl)
                    }
            )
        }

        return UserSubscriptionResponse(feeds = feeds)
    }

    @GetMapping("/explore-feed/{channelId}")
    fun exploreFeed(
        @PathVariable channelId: UUID
    ): RssChannelResponse? {
        return RssChannelRecord.findById(channelId)?.let {
            rssReaderService
                .getFeed(it.channelUrl)
                .toRssChannelResponse(channelId, it.channelUrl)
        }
    }

}