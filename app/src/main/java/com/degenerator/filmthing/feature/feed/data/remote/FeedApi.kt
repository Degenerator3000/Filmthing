package com.degenerator.filmthing.feature.feed.data.remote

import com.degenerator.filmthing.feature.feed.data.dto.FilmsPageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedApi {
    /**
     * Строит и отправляет запрос к api
     * @param next курсор для получения следующей порции фильмов
     * @param limit количество фильмов в порции
     * @param sortField выбор поля для осуществления сортировки.
     * Значение по умолчанию - id
     * @param sortType тип сортировки (1 - asc, -1 - desc)
     * Значение по умолчанию - 1
     * @param selectFields Получаемые поля. Значение по умолчанию:
     * id, names, shortDescription, poster, rating
     * @return [FilmsPageDto] с фильмами и курсором для следующего запроса
     */
    @GET("v1.5/movie")
    suspend fun getMovies(
        @Query("next") next: String?,
        @Query("limit") limit: Int = 20,
        @Query("sortField") sortField: String = "id",
        @Query("sortType") sortType: String = "1",
        @Query("selectFields") selectFields: List<String> = listOf(
            "id", "names", "shortDescription", "poster", "rating"
        )
    ): FilmsPageDto
}
