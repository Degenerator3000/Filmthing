package com.degenerator.filmthing.feature.feed.domain.usecase
import com.degenerator.filmthing.feature.feed.domain.entity.FilmsPage
import com.degenerator.filmthing.feature.feed.domain.repository.FeedRepository

/**
 * Получение группы случайных фильмов
 */

class GetRandomFilmsUseCase(private val feedRepository: FeedRepository) {
    /**
     * @param cursor токен для получения следующей порции; null — первый запрос
     * @param limit количество фильмов в порции; на последней порции может быть меньше
     * @return [FilmsPage] с фильмами и курсором для следующего запроса
     * @throws java.io.IOException при ошибке сети или парсинга ответа
     */
    suspend operator fun invoke(cursor: String?, limit: Int): FilmsPage = feedRepository.getRandomFilms(cursor, limit)
}
