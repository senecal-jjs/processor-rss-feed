package com.rss.service

import com.rss.api.RssChannelResponse
// import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File

@Service
@EnableScheduling
class RssSearchService {
    private val logger = LoggerFactory.getLogger(RssSearchService::class.java)
//    val model = WordVectorSerializer.loadTxtVectors(File("/Users/jacobsenecal/Repos/Personal/processor-rss-feed/service/src/main/kotlin/com/rss/service/glove.6B/glove.6B.100d.txt"))
//    val vec = model.getWordVector("technology")
//
//    @Scheduled(fixedRate = 5000L)
//    fun printVec() {
//        logger.info("tech vector $vec")
//    }

//    fun searchFeeds(searchTerm: String): List<RssChannelResponse> {
//
//    }
}