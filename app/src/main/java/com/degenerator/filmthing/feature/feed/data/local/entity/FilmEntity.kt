package com.degenerator.filmthing.feature.feed.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val shortDescription: String?,
    val posterUrl: String?,
    val rating: Double?,
)
