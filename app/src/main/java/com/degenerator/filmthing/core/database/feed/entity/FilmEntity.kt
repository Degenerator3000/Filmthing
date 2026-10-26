package com.degenerator.filmthing.core.database.feed.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Представление фильма в базе данных
 *
 * @property id первичный ключ
 */
@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val shortDescription: String?,
    val posterUrl: String?,
    val rating: Double?,
)
