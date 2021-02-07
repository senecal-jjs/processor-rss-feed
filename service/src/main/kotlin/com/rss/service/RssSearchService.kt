package com.rss.service

import com.rss.api.response.RssChannelResponse
import com.rss.data.RssChannel
import com.rss.data.RssChannelRecord
import com.rss.extension.toUuid
import kong.unirest.Unirest
import org.jetbrains.exposed.sql.transactions.transaction
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
            .slice(0..5)
            .mapNotNull { score ->
                transaction {
                    RssChannelRecord.findById(score.id)?.toResponse()
                }
            }

        return (listOf(findExactMatch(searchTerm)) + top5).filterNotNull()
    }

    private fun findExactMatch(searchTerm: String): RssChannelResponse? = transaction {
        RssChannelRecord.find {
            RssChannel.channelUrl eq searchTerm
        }
        .firstOrNull()
        ?.toResponse()
    }
}

data class Score(
    val id: UUID,
    val score: Float
)