package com.rss.service

import com.rss.api.response.FeedSearchResponse
import com.rss.api.response.RssChannelResponse
import com.rss.data.exposed.RssChannel
import com.rss.data.exposed.RssChannelRecord
import com.rss.extension.toUuid
import kong.unirest.Unirest
import org.apache.commons.text.similarity.FuzzyScore
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class RssSearchService {
    private val logger = LoggerFactory.getLogger(RssSearchService::class.java)

    /**
     * Perform a fuzzy search based on the actual textual values
     */
    fun fuzzySearch(
        searchTerm: String,
        limit: Int = 5
    ): FeedSearchResponse {
        val topResults = mutableListOf<Pair<Int, RssChannelResponse>>()
        val channels: List<RssChannelRecord> = transaction { RssChannelRecord.all().toList() }
        var thresholdScore = 0

        channels.forEach { channel ->
            getHighestScoreFromList(
                channel.topicItem.topics + channel.title.split(" ") + channel.channelUrl + channel.channelDesc.split(" "),
                searchTerm
            ).let { highScore ->
                if (topResults.size >= limit) {
                    if (highScore > thresholdScore) {
                        topResults.sortByDescending { it.first }
                        topResults[topResults.size - 1] = Pair(highScore, channel.toResponse())
                    }
                } else {
                    topResults.add(Pair(highScore, channel.toResponse()))
                }
                thresholdScore = topResults.minByOrNull { it.first }?.first ?: 0
            }
        }

        return FeedSearchResponse(
            feeds = (listOf(findExactMatch(searchTerm)) + topResults.map { it.second }).filterNotNull()
        )
    }

    /**
     * Perform a search taking into account the semantics of the query
     */
    fun semanticSearch(searchTerm: String): FeedSearchResponse {
        val scores: MutableList<Score> = mutableListOf()

        Unirest.get("http://localhost:5000/similarity-search/$searchTerm")
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

        return FeedSearchResponse(
            feeds = (listOf(findExactMatch(searchTerm)) + top5).filterNotNull()
        )
    }

    private fun findExactMatch(searchTerm: String): RssChannelResponse? = transaction {
        RssChannelRecord
            .find { RssChannel.channelUrl eq searchTerm }
            .firstOrNull()
            ?.toResponse()
    }

    private fun getFuzzyScore(term1: String, term2: String): Int {
        return FuzzyScore(Locale.ENGLISH).fuzzyScore(term1, term2)
    }

    private fun getHighestScoreFromList(potentialMatches: List<String>, searchTerm: String): Int {
        return potentialMatches
            .map { potentialMatch -> getFuzzyScore(searchTerm, potentialMatch) }
            .maxOrNull() ?: 0
    }
}

data class Score(
    val id: UUID,
    val score: Float
)