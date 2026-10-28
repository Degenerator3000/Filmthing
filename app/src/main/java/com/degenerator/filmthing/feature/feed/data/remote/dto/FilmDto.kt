package com.degenerator.filmthing.feature.feed.data.remote.dto

import kotlinx.serialization.Serializable
/**
 * Сетевое представление фильма из ответа Kinopoisk API.
 * Маппится в сущность БД через toEntity()
 *
 * @property names список названий фильма на разных языках
 */
@Serializable
data class FilmDto(
    val id: Int,
    val names: List<NameDto> = emptyList(),
    val shortDescription: String? = null,
    val poster: PosterDto? = null,
    val rating: RatingDto? = null,
)

/**
 * Элемент списка альтернативных названий фильма
 */
@Serializable
data class NameDto(val language: String?, val name: String)

/**
 * Постер фильма. Содержит полный и превью URL;
 * для карточки ленты используется [url].
 */
@Serializable
data class PosterDto(val url: String? = null)

/**
 * Рейтинги фильма из разных источников.
 * В сущность маппится только [kp] (Кинопоиск)
 */
@Serializable
data class RatingDto(val kp: Double? = null)
