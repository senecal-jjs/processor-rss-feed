package com.rss.web

import com.rss.api.FeedSubscriptionRequest
import com.rss.data.RssChannel
import com.rss.data.Subscription
import com.rss.security.Session
import com.rss.service.RssSearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search-feeds")
class FeedController(private val rssSearchService: RssSearchService) {
    @GetMapping("/search-feeds")
    fun searchFeeds(
        @RequestParam searchTerm: String
    ) {

    }

    @PostMapping("/register-feed")
    fun registerFeed(
        @RequestBody feedSubscriptionRequest: FeedSubscriptionRequest
    ) {
        val channelId = RssChannel.getChannelIdByUrl(feedSubscriptionRequest.url)

        channelId?.run {
            Subscription.subscribe(
                Session.uuid(),
                channelId,
                feedSubscriptionRequest.url,
                feedSubscriptionRequest.category
            )
        }
    }
}