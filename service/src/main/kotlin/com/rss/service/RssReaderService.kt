package com.rss.service

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import com.rss.data.RssChannel
import com.rss.data.Topics
import org.springframework.stereotype.Service
import java.net.URL
import java.util.UUID

@Service
class RssReaderService {
    fun getFeed(url: String): SyndFeed {
        return SyndFeedInput()
            .build(XmlReader(URL(url)))
    }

    fun saveFeed(url: String, topics: Topics): UUID {
        val feed = getFeed(url)

        return RssChannel.save(
            feed.title,
            feed.link,
            url,
            feed.description,
            topics
        )
    }
}