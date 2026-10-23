package com.degenerator.filmthing.feature.feed.domain.entity

/**
 * Результат поискового запроса
 *
 * @property nextCursor токен для получения следующей группы.
 * Null означает, что результатов больше нет
 */
data class FilmsPage(val films: List<Film>, val nextCursor: String?)
