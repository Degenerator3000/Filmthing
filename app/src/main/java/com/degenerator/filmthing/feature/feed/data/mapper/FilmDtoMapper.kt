package com.degenerator.filmthing.feature.feed.data.mapper

import com.degenerator.filmthing.feature.feed.data.remote.dto.FilmDto
import com.degenerator.filmthing.feature.feed.domain.entity.Film

/**
 * Маппинг сетевого представления в доменную [Film]
 *
 * @return [Film] или null если у DTO нет ни одного названия
 */
fun FilmDto.toEntity(): Film? {
    val resolvedName = names.firstOrNull { it.language == "RU" }?.name
        ?: names.firstOrNull()?.name
        ?: return null
    return Film(
        id = id,
        name = resolvedName,
        shortDescription = shortDescription,
        posterUrl = poster?.url,
        rating = rating?.kp,
    )
}
