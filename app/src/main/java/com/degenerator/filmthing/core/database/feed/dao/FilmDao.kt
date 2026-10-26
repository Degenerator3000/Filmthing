package com.degenerator.filmthing.core.database.feed.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.degenerator.filmthing.core.database.feed.entity.FilmEntity

/**
 * Кэш фильмов ленты в Room. Источник данных для Paging 3
 */
@Dao
interface FilmDao {
    @Query("SELECT * FROM films ORDER BY id")
    fun getFilmsPagingSource(): PagingSource<Int, FilmEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(films: List<FilmEntity>)

    @Query("DELETE FROM films")
    suspend fun clearAll()
}
