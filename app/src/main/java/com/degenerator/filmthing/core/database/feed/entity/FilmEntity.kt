package com.degenerator.filmthing.core.database.feed.entity

import androidx.room.Entity

/**
 * Представление фильма в базе данных
 *
 * @property id идентификатор конкретного фильма
 * @property queryKey идентификатор для привязки фильма к циклу пагинации
 * @property sortOrder порядок получения карточки из апи. Нужен для корректной работы Paging
 */
@Entity(tableName = "films", primaryKeys = ["id", "queryKey"])
data class FilmEntity(
    val id: Int,
    val queryKey: String,
    val sortOrder: Int,
    val name: String,
    val shortDescription: String?,
    val posterUrl: String?,
    val rating: Double?,
)
