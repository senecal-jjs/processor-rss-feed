package com.rss.service

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import com.rss.data.RssChannelRecord
import com.rss.data.TopicItem
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.net.URL
import java.util.UUID

@Service
class RssReaderService {
    fun getFeed(url: String): SyndFeed {
        return SyndFeedInput()
            .build(XmlReader(URL(url)))
    }

    fun saveFeed(url: String, topics: TopicItem): UUID {
        val feed = getFeed(url)

        val id = UUID.randomUUID()

        transaction {
            RssChannelRecord.new(id) {
                this.title = feed.title
                this.siteUrl = feed.link
                this.channelUrl = url
                this.channelDesc = feed.description
                this.topicItem = topics
            }
        }

        return id
    }
}