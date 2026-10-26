package com.degenerator.filmthing.feature.feed.data.mapper

import com.degenerator.filmthing.feature.feed.data.local.entity.FilmEntity
import com.degenerator.filmthing.feature.feed.data.remote.dto.FilmDto

/**
 * Маппинг сетевого представления в сущность бд
 *
 * @return [FilmEntity] или null если у DTO нет ни одного названия
 */
fun FilmDto.toEntity(): FilmEntity? {
    val resolvedName = names.firstOrNull { it.language == "RU" }?.name
        ?: names.firstOrNull()?.name
        ?: return null
    return FilmEntity(
        id = id,
        name = resolvedName,
        shortDescription = shortDescription,
        posterUrl = poster?.url,
        rating = rating?.kp,
    )
}
