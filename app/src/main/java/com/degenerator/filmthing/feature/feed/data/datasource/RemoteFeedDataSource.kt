package com.degenerator.filmthing.feature.feed.data.datasource

import com.degenerator.filmthing.feature.feed.data.dto.FilmsPageDto
import com.degenerator.filmthing.feature.feed.data.remote.FeedApi

/**
 * Делегирует запрос в [FeedApi]
 */
class RemoteFeedDataSource(
    private val feedApi: FeedApi
) : FeedDataSource {
    override suspend fun fetchFilms(cursor: String?, limit: Int): FilmsPageDto {
        return feedApi.getMovies(next = cursor, limit = limit)
    }
}

