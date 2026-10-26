package com.degenerator.filmthing.core.database.feed.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.degenerator.filmthing.core.database.feed.entity.RemoteKeyEntity

/**
 * Хранит курсоры пагинации в Room. Один курсор на queryKey
 */
@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE queryKey = :queryKey")
    suspend fun getRemoteKey(queryKey: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE queryKey = :queryKey")
    suspend fun deleteByQuery(queryKey: String)
}
