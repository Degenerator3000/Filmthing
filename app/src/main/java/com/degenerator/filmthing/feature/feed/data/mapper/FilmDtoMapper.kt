package com.degenerator.filmthing.feature.feed.data.mapper

import com.degenerator.filmthing.core.database.feed.entity.FilmEntity
import com.degenerator.filmthing.feature.feed.data.remote.dto.FilmDto

/**
 * Маппинг сетевого представления в сущность бд
 *
 * @return [FilmEntity] или null если у DTO нет ни одного названия
 */
fun FilmDto.toEntity(queryKey: String, sortOrder: Int): FilmEntity? {
    val resolvedName = names.firstOrNull { it.language == "RU" }?.name
        ?: names.firstOrNull()?.name
        ?: return null
    return FilmEntity(
        id = id,
        queryKey = queryKey,
        sortOrder = sortOrder,
        name = resolvedName,
        shortDescription = shortDescription,
        posterUrl = poster?.url,
        rating = rating?.kp,
    )
}
