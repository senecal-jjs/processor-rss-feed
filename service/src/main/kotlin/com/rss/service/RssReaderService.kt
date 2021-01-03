package com.rss.service

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.springframework.stereotype.Service
import java.net.URL

@Service
class RssReaderService {
    fun getFeed(url: String): SyndFeed {
        return SyndFeedInput()
            .build(XmlReader(URL(url)))
    }
}