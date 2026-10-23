package com.degenerator.filmthing.feature.feed.domain.entity

/**
 * Минимальное необходимое представление фильма.
 *
 * Все поля — без изменений из дб
 */
data class Film(
    val id: Int,
    val name: String,
    val shortDescription: String?,
    val posterUrl: String?,
    val rating: Double?,
)
