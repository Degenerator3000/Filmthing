package com.degenerator.filmthing.feature.feed.data.mapper

import com.degenerator.filmthing.feature.feed.data.local.entity.FilmEntity
import com.degenerator.filmthing.feature.feed.domain.entity.Film

fun FilmEntity.toDomain(): Film = Film(
    id = id,
    name = name,
    shortDescription = shortDescription,
    posterUrl = posterUrl,
    rating = rating,
)
