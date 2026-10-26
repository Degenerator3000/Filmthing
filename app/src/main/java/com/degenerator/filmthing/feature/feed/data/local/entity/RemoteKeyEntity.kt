package com.degenerator.filmthing.feature.feed.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val queryKey: String,
    val nextKey: String?,
)
