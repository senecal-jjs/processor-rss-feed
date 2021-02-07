package com.rss.service

import com.rss.api.RssChannelResponse
import com.rss.data.RssChannel
import com.rss.extension.toUuid
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service
import java.util.*

@Service
@EnableScheduling
class RssSearchService {
    private val logger = LoggerFactory.getLogger(RssSearchService::class.java)


    fun searchFeeds(searchTerm: String): List<RssChannelResponse> {
        val scores: MutableList<Score> = mutableListOf()

        Unirest.get("http://localhost:5000/$searchTerm")
            .asJson()
            .ifSuccess { response ->
                response.body.`object`.let { result ->
                    result.keys().forEach {
                        scores.add(
                            Score(
                                id = it.toUuid(),
                                score = result[it].toString().toFloat()
                            )
                        )
                    }
                }
            }
            .ifFailure { response ->
                response.parsingError.ifPresent { e ->
                    logger.error("Parsing exception $e")
                }
            }

        scores.sortBy { it.score }
        val top5 = scores
            .map { it.id }.slice(0..5)

        return findExactMatch(searchTerm)?.let {
            (top5 + it).map {  }
        }
    }

    private fun MutableList<Score>.getMax(): Score? {
        return this.maxByOrNull { it.score }
    }

    private fun findExactMatch(searchTerm: String): UUID? {
        RssChannel.getAllChannelUrlsAndId().forEach {
            if (it.second.toLowerCase().replace(" ", "") == searchTerm.toLowerCase().replace(" ", "")) {
                return it.first
            }
        }
        return null
    }
}

data class Score(
    val id: UUID,
    val score: Float
)