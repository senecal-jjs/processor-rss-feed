package service

import com.rss.service.RssReaderService
import org.junit.jupiter.api.Test

class RssReaderServiceTest {
    private val rssReaderService = RssReaderService()

    @Test
    fun `read rss url`() {
        val url = "http://feeds.arstechnica.com/arstechnica/index/"

        val feed = rssReaderService.getFeed(url)
        println(feed.entries.first())
    }
}