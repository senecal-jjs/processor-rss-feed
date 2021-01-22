package com.rss.web

import com.rss.api.FeedSubscriptionRequest
import com.rss.data.RssChannel
import com.rss.data.Subscription
import com.rss.data.Topics
import com.rss.security.Session
import com.rss.service.RssReaderService
import com.rss.service.RssSearchService
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
    ) {

    }

    @PostMapping("/register-feed")
    fun registerFeed(
        @RequestBody feedSubscriptionRequest: FeedSubscriptionRequest
    ) {
        // save new feed if it doesn't exist
        val channelId = RssChannel.getChannelIdByUrl(feedSubscriptionRequest.url) ?: let {
            rssReaderService.saveFeed(
                feedSubscriptionRequest.url,
                Topics(topics = listOf(feedSubscriptionRequest.category))
            )
        }

        Subscription.subscribe(
            Session.uuid(),
            channelId,
            feedSubscriptionRequest.url,
            feedSubscriptionRequest.category
        )
    }
}