package com.rss.extension

import com.rometools.rome.feed.synd.SyndFeed
import com.rss.api.response.RssChannelResponse
import com.rss.api.response.RssItemResponse
import java.util.*

fun SyndFeed.toRssChannelResponse(
    channelId: UUID,
    channelUrl: String
): RssChannelResponse {
    return RssChannelResponse(
        id = channelId,
        title = this.title,
        siteUrl = this.link,
        channelUrl = channelUrl,
        description = this.description,
        pubDate = this.publishedDate.toOffsetDateTime(),
        lastBuildDate = this.publishedDate.toOffsetDateTime(),
        imageUrl = this.image.url,
        items = this.entries.map { entry ->
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