package com.degenerator.filmthing.feature.feed.data.datasource

import com.degenerator.filmthing.feature.feed.data.dto.FilmsPageDto

/**
 * Источник данных для ленты фильмов.
 * Реализация может ходить в сеть, кэш или комбинировать их.
 */
interface FeedDataSource {
    /**
     * @param cursor токен для следующей порции; null — первая порция
     * @param limit количество фильмов в порции
     * @return [FilmsPageDto] с фильмами и новым курсором; null — последняя порция
     * @throws java.io.IOException при ошибке сети
     */
    suspend fun fetchFilms(cursor: String?, limit: Int): FilmsPageDto
}
