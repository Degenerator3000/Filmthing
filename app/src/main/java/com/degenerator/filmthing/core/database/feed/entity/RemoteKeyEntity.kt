package com.degenerator.filmthing.core.database.feed.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Представление текущего состояния пагинации в бд
 *
 * @property queryKey первичный ключ. Представляет поисковый запрос
 * @property nextKey курсор для получения следующей порции
 */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val queryKey: String,
    val nextKey: String?,
)
