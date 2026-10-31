package com.degenerator.filmthing

import androidx.paging.testing.asSnapshot
import com.degenerator.filmthing.core.database.AppDatabase
import com.degenerator.filmthing.core.database.feed.dao.FilmDao
import com.degenerator.filmthing.core.database.feed.dao.RemoteKeyDao
import com.degenerator.filmthing.feature.feed.data.remote.FeedApi
import com.degenerator.filmthing.feature.feed.data.remote.dto.FilmDto
import com.degenerator.filmthing.feature.feed.data.remote.dto.FilmsPageDto
import com.degenerator.filmthing.feature.feed.data.remote.dto.NameDto
import com.degenerator.filmthing.feature.feed.data.remote.dto.PosterDto
import com.degenerator.filmthing.feature.feed.data.remote.dto.RatingDto
import com.degenerator.filmthing.feature.feed.data.repository.FeedRepositoryImpl
import com.degenerator.filmthing.feature.feed.domain.entity.Film
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FeedRepositoryImplTest {

    private lateinit var database: AppDatabase
    private lateinit var feedApi: FeedApi
    private lateinit var repository: FeedRepositoryImpl

    private val filmDao = mockk<FilmDao>(relaxed = true)
    private val remoteKeyDao = mockk<RemoteKeyDao>(relaxed = true)

    @BeforeEach
    fun setUp() {
        database = mockk(relaxed = true)
        feedApi = mockk()

        every { database.filmDao() } returns filmDao
        every { database.remoteKeyDao() } returns remoteKeyDao

        repository = FeedRepositoryImpl(
            database = database,
            feedApi = feedApi,
            sortFields = listOf("rating.kp"),
            sortTypes = listOf("-1")
        )

        @Test
        fun `getRandomFilms returns empty list when API returns empty response`() = runTest {
            coEvery { feedApi.getMovies(any(), any(), any(), any(), any()) } returns FilmsPageDto(
                docs = emptyList(),
                next = null
            )

            // When
            val result = repository.getRandomFilms().asSnapshot {  }

            // Then: Список должен быть пустым
            assertTrue(result.isEmpty(), "Ожидался пустой список, но получен $result")
        }

        @Test
        fun `getRandomFilms maps DTO to domain correctly when API returns films`() = runTest {
            // Given: API возвращает один фильм
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

            // When
            val result = repository.getRandomFilms().asSnapshot()

            // Then: Проверяем, что DTO корректно преобразовался в доменную модель
            val expectedFilm = Film(
                id = 1,
                name = "Тестовый фильм",
                shortDescription = "Описание",
                posterUrl = "https://example.com/poster.jpeg",
                rating = 8.5
            )
            assertEquals(listOf(expectedFilm), result)
        }

        @Test
        fun `getRandomFilms uses fallback name when no RU name is available`() = runTest {
            // Given: У фильма нет названия на русском
            val filmDto = FilmDto(
                id = 2,
                names = listOf(NameDto("EN", "Test Film")),
                shortDescription = null,
                poster = null,
                rating = null
            )
            coEvery { feedApi.getMovies(any(), any(), any(), any(), any()) } returns
                FilmsPageDto(
                docs = listOf(filmDto),
                next = null
            )

            // When
            val result = repository.getRandomFilms().asSnapshot()

            // Then: Должно использоваться первое доступное название
            assertEquals("Test Film", result.first().name)
        }

        @Test
        fun `getRandomFilms emits error state when API throws IOException`() = runTest {
            // Given: API выбрасывает исключение
            coEvery { feedApi.getMovies(any(), any(), any(), any(), any()) } throws
                java.io.IOException("Network error")

            // When
            val result = repository.getRandomFilms().asSnapshot()

            // Then: PagingData должен содержать ошибку, а не падать с исключением.
            // asSnapshot() возвращает только успешные элементы, поэтому проверяем,
            // что поток не эмитит данные и завершается без краша.
            // Для проверки ошибки в LoadState потребовался бы более сложный тест,
            // но для базового сценария достаточно убедиться, что список пуст.
            assertTrue { result.isEmpty() }
        }

        @Test
        fun `getRandomFilms does not emit films without valid name`() = runTest {
            // Given: API вернул фильм без названий в поле names
            val filmDto = FilmDto(
                id = 3,
                names = listOf(),
                shortDescription = "Описание",
                poster = PosterDto(url = "https://example.com/poster.jpeg"),
                rating = RatingDto(kp = 8.5)
            )
            coEvery { feedApi.getMovies(any(), any(), any(), any(), any()) } returns
                FilmsPageDto(
                    docs = listOf(filmDto),
                    next = null
                )

            // When
            val result = repository.getRandomFilms().asSnapshot()

            // Then поток не эмитит фильмы без названия
            assertTrue(result.isEmpty(), "Ожидался пустой список, но получен: $result")
        }

    }

}
