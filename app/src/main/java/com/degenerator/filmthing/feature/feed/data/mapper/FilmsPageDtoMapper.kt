package com.degenerator.filmthing.feature.feed.data.mapper

import com.degenerator.filmthing.feature.feed.data.dto.FilmsPageDto
import com.degenerator.filmthing.feature.feed.domain.entity.FilmsPage

/**
 * Маппинг сетевого представления в доменную [FilmsPage]
 *
 * Фильтрует фильмы без названия
 */
fun FilmsPageDto.toEntity(): FilmsPage = FilmsPage(
    films = docs.mapNotNull { it.toEntity() },
    nextCursor = next,
)
