package com.degenerator.filmthing.feature.feed.domain.repository

import androidx.paging.PagingData
import com.degenerator.filmthing.feature.feed.domain.entity.Film
import kotlinx.coroutines.flow.Flow

/**
 * Контракт доступа к данным о фильмах для фичи ленты.
 *
 * Реализация получает данные из сети.
 */
interface FeedRepository {

    /**
     * Возвращает поток страниц случайных фильмов.
     *
     * Параметры сортировки выбираются один раз при вызове и фиксируются
     * на весь цикл пагинации. Ошибки сети приходят через
     * [androidx.paging.LoadState] в [PagingData], исключения не бросаются.
     *
     * @return [Flow] с [PagingData] доменных [Film]
     */
    fun getRandomFilms(): Flow<PagingData<Film>>
}
