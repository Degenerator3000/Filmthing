package com.degenerator.filmthing.feature.feed.data.mapper

import com.degenerator.filmthing.core.database.feed.entity.FilmEntity
import com.degenerator.filmthing.feature.feed.domain.entity.Film

/**
 * Маппит представление из бд в доменную [Film]
 */
fun FilmEntity.toDomain(): Film = Film(
    id = id,
    name = name,
    shortDescription = shortDescription,
    posterUrl = posterUrl,
    rating = rating,
)
