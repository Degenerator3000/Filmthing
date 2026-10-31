package com.degenerator.filmthing

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.degenerator.filmthing.core.database.AppDatabase
import com.degenerator.filmthing.core.database.feed.dao.FilmDao
import com.degenerator.filmthing.core.database.feed.dao.RemoteKeyDao
import com.degenerator.filmthing.core.database.feed.entity.FilmEntity
import com.degenerator.filmthing.core.database.feed.entity.RemoteKeyEntity
import com.degenerator.filmthing.feature.feed.data.remote.FeedApi
import com.degenerator.filmthing.feature.feed.data.remote.FeedRemoteMediator
import com.degenerator.filmthing.feature.feed.data.remote.dto.FilmDto
import com.degenerator.filmthing.feature.feed.data.remote.dto.FilmsPageDto
import com.degenerator.filmthing.feature.feed.data.remote.dto.NameDto
import com.degenerator.filmthing.feature.feed.data.remote.dto.PosterDto
import com.degenerator.filmthing.feature.feed.data.remote.dto.RatingDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FeedRemoteMediatorTest {

    private val queryKey = "feed_rating.kp_-1"
    private val sortField = "rating.kp"
    private val sortType = "-1"
    private lateinit var database: AppDatabase
    private lateinit var feedApi: FeedApi
    private lateinit var mediator: FeedRemoteMediator

    private val filmDao = mockk<FilmDao>(relaxed = true)
    private val remoteKeyDao = mockk<RemoteKeyDao>(relaxed = true)

    @OptIn(ExperimentalPagingApi::class)
    @BeforeEach
    fun setUp() {
        database = mockk(relaxed = true)
        feedApi = mockk()

        every { database.filmDao() } returns filmDao
        every { database.remoteKeyDao() } returns remoteKeyDao

        mediator = FeedRemoteMediator(
            queryKey = queryKey,
            sortField = sortField,
            sortType = sortType,
            database = database,
            feedApi = feedApi
        )

        @Test
        fun `FeedRemoteMediator correctly loads page when LoadType is REFRESH`() = runTest {
            val pagingState = PagingState<Int, FilmEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0,
            )

            val filmDto = FilmDto(
                id = 1,
                names = listOf(NameDto("RU", "Тестовый фильм")),
                shortDescription = "Описание",
                poster = PosterDto(url = "https://example.com/poster.jpeg"),
                rating = RatingDto(kp = 8.5)
            )
            coEvery { feedApi.getMovies(any(), any(), any(), any(), any()) } returns
                FilmsPageDto(
                    docs = listOf(filmDto),
                    next = null
                )

            val result = mediator.load(loadType =  LoadType.REFRESH, state = pagingState)

            coVerify  { feedApi.getMovies(
                next = null,
                limit = any(),
                sortField = any(),
                sortType = any(),
                selectFields = any(),
            ) }

            coVerify { filmDao.deleteByQuery(queryKey) }

            coVerify { remoteKeyDao.insertOrReplace(RemoteKeyEntity(queryKey, null)) }

            coVerify {
                filmDao.insertAll(
                    match { films ->
                        films.size == 1 && films[0].sortOrder == 0 && films[0].id == 1
                    }
                )
            }

            assertEquals(RemoteMediator.MediatorResult.Success(endOfPaginationReached = false), result)
        }
    }
}
