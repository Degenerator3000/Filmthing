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
    @Query("SELECT * FROM films WHERE queryKey = :queryKey ORDER BY sortOrder ASC")
    fun getFilmsPagingSource(queryKey: String): PagingSource<Int, FilmEntity>

    @Query("SELECT MAX(sortOrder) FROM films WHERE queryKey = :queryKey")
    suspend fun getMaxSortOrder(queryKey: String): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(films: List<FilmEntity>)

    @Query("DELETE FROM films WHERE queryKey LIKE 'feed_%' AND queryKey != :currentKey")
    suspend fun deleteFeedFilmsExcept(currentKey: String)

    @Query("DELETE FROM films WHERE queryKey = :queryKey")
    suspend fun deleteByQuery(queryKey: String)
}
