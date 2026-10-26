package com.degenerator.filmthing.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.degenerator.filmthing.feature.feed.data.local.dao.FilmDao
import com.degenerator.filmthing.feature.feed.data.local.dao.RemoteKeyDao
import com.degenerator.filmthing.feature.feed.data.local.entity.FilmEntity
import com.degenerator.filmthing.feature.feed.data.local.entity.RemoteKeyEntity

@Database(
    entities = [FilmEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
