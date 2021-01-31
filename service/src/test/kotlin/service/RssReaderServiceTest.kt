package service

import com.rss.service.RssReaderService
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.junit.jupiter.api.Test
import java.io.File

class RssReaderServiceTest {
    private val rssReaderService = RssReaderService()

    @Test
    fun `read rss url`() {
        val url = "http://feeds.arstechnica.com/arstechnica/index/"

        val feed = rssReaderService.getFeed(url)
        println(feed)
    }

    @Test
    fun `make word vector`() {
        val model = WordVectorSerializer.readWordVectors(File("/Users/jacobsenecal/Repos/Personal/processor-rss-feed/service/src/main/kotlin/com/rss/service/glove.6B/glove.6B.100d.txt"))
//        val vec = model.getWordVector("technology")
//        println("$vec")
    }
}