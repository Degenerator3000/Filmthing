package com.degenerator.filmthing.feature.feed.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import com.degenerator.filmthing.core.database.AppDatabase
import com.degenerator.filmthing.core.database.feed.dao.FilmDao
import com.degenerator.filmthing.core.database.feed.entity.FilmEntity
import com.degenerator.filmthing.core.database.feed.entity.RemoteKeyEntity
import com.degenerator.filmthing.feature.feed.data.mapper.toDomain
import com.degenerator.filmthing.feature.feed.data.remote.FeedApi
import com.degenerator.filmthing.feature.feed.data.remote.FeedRemoteMediator
import com.degenerator.filmthing.feature.feed.domain.entity.Film
import com.degenerator.filmthing.feature.feed.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Реализация [FeedRepository] на Paging 3 + Room.
 *
 * Каждый вызов [getRandomFilms] создаёт новый [Pager] со случайной
 * комбинацией сортировки: это сбрасывает пагинацию и запускает
 * новый цикл с чистым курсором.
 *
 * @param sortFields допустимые значения `sortField` для API. Не должен быть пустым
 * @param sortTypes допустимые значения `sortType` ("1" / "-1")
 */
class FeedRepositoryImpl(
    private val database: AppDatabase,
    private val feedApi: FeedApi,
    private val sortFields: List<String>,
    private val sortTypes: List<String>,
) : FeedRepository {

    /**
     * Собирает `queryKey` из случайных параметров сортировки.
     * Используется как ключ курсора в [RemoteKeyEntity] и как
     * разделитель независимых циклов пагинации.
     */
    private fun newQueryKey(): Pair<String, Pair<String, String>> {
        val sortField = sortFields.random()
        val sortType = sortTypes.random()
        return "feed_${sortField}_${sortType}" to (sortField to sortType)
    }

    /**
     * Возвращает поток страниц случайных фильмов.
     *
     * [RemoteMediator] грузит данные из [FeedApi] в Room,
     * [Pager] читает их через [FilmDao] и маппит
     * [FilmEntity] в [Film] до попадания в UI.
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun getRandomFilms(): Flow<PagingData<Film>> {
        val (queryKey, params) = newQueryKey()
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 40,
            ),
            remoteMediator = FeedRemoteMediator(
                queryKey = queryKey,
                sortField = params.first,
                sortType = params.second,
                database = database,
                feedApi = feedApi,
            ),
            pagingSourceFactory = { database.filmDao().getFilmsPagingSource(queryKey) },
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }
}
