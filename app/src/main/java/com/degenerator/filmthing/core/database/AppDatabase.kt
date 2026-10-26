package com.degenerator.filmthing.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.degenerator.filmthing.core.database.feed.dao.FilmDao
import com.degenerator.filmthing.core.database.feed.dao.RemoteKeyDao
import com.degenerator.filmthing.core.database.feed.entity.FilmEntity
import com.degenerator.filmthing.core.database.feed.entity.RemoteKeyEntity

/**
 * Единая база данных приложения.
 * Список сущностей расширяется по мере добавления фич.
 */
@Database(
    entities = [FilmEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
