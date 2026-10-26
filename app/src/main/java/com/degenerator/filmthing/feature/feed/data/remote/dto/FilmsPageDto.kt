package com.degenerator.filmthing.feature.feed.data.remote.dto

import com.degenerator.filmthing.feature.feed.domain.entity.FilmsPage
import kotlinx.serialization.Serializable

/**
 * Сетевое представление порции фильмов из ответа Kinopoisk API.
 * Маппится в [FilmsPage] через toEntity()
 *
 * @property docs список фильмов в текущей порции
 * @property next курсор для получения следующей порции;
 * null означает, что порций больше нет
 */
@Serializable
data class FilmsPageDto(val docs: List<FilmDto> = emptyList(), val next: String? = null)
