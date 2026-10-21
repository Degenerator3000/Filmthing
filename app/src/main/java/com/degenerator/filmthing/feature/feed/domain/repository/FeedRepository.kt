package com.degenerator.filmthing.feature.feed.domain.repository

import com.degenerator.filmthing.feature.feed.domain.entity.FilmsPage

/**
 * Контракт доступа к данным о фильмах для фичи ленты.
 *
 * Реализация будет получать данные из сети.
 * Домен не знает, откуда приходят данные.
 */
interface FeedRepository {

    /**
     * Возвращает одну страницу случайных фильмов.
     *
     * @param cursor токен для получения следующей порции; null для первого запроса
     * @param limit количество фильмов в порции; на последней порции может быть меньше
     * @return [FilmsPage] с фильмами и курсором для следующего запроса
     * @throws java.io.IOException если произошла ошибка сети или парсинга.
     */
    suspend fun getRandomFilms(cursor: String?, limit: Int): FilmsPage
}
