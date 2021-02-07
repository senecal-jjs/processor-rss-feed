package com.rss.service

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import com.rss.data.RssChannel
import com.rss.data.RssChannelRecord
import com.rss.data.Topics
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

    fun saveFeed(url: String, topics: Topics): UUID {
        val feed = getFeed(url)

        val id = UUID.randomUUID()

        transaction {
            RssChannelRecord.new(id) {
                this.title = feed.title
                this.siteUrl = feed.link
                this.channelUrl = url
                this.channelDesc = feed.description
                this.topics = topics
            }
        }

        return id
    }
}