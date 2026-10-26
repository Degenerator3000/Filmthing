package com.degenerator.filmthing.feature.feed.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilmDao {
    @Query("SELECT * FROM films ORDER BY id")
    fun getFilmsPagingSource(): PagingSource<Int, RemoteKeyDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(films: List<RemoteKeyDao>)

    @Query("DELETE FROM films")
    suspend fun clearAll()
}
