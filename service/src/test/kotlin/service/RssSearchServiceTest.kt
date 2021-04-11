package service

import com.rss.data.RssChannel
import com.rss.data.RssChannelRecord
import com.rss.data.TopicItem
import com.rss.service.RssSearchService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import util.DbUtils
import java.util.UUID

class RssSearchServiceTest {
    @Test
    fun `basic fuzzy search works`() {
        val searchService = RssSearchService()

        DbUtils.connect(RssChannel) {
            RssChannelRecord.new(UUID.randomUUID()) {
                topicItem = TopicItem(topics = listOf("basketball, sports, espn"))
                title = "ESPN TOP 10"
                channelUrl = "https://www.espn.com"
                channelDesc = "Top 10 basketball plays"
            }

            RssChannelRecord.new(UUID.randomUUID()) {
                topicItem = TopicItem(topics = listOf("golf, sports, espn"))
                title = "ESPN TOP 10"
                channelUrl = "https://www.espn.com"
                channelDesc = "Top 10 golf plays"
            }


            searchService.fuzzySearch("basketball", RssChannelRecord.all(), limit = 1).run {
                Assertions.assertEquals(this.first().description, "Top 10 basketball plays")
            }
        }
    }
}