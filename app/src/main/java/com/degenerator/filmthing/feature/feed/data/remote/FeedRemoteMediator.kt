package com.degenerator.filmthing.feature.feed.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.degenerator.filmthing.core.database.AppDatabase
import com.degenerator.filmthing.core.database.feed.entity.FilmEntity
import com.degenerator.filmthing.core.database.feed.entity.RemoteKeyEntity
import com.degenerator.filmthing.feature.feed.data.mapper.toEntity
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
/**
 * Мост между [FeedApi] и Room для ленты фильмов.
 *
 * Курсор пагинации хранится в [RemoteKeyEntity] (ключ — [queryKey]).
 * Параметры поиска фиксируются в конструкторе на весь цикл пагинации,
 * чтобы курсор оставался валидным между запросами.
 *
 * @param queryKey ключ, разделяющий независимые циклы пагинации
 * @param sortField по какому параметру сортировать
 * @param sortType направление сортировки
 */
class FeedRemoteMediator(
    private val queryKey: String,
    private val sortField: String,
    private val sortType: String,
    private val database: AppDatabase,
    private val feedApi: FeedApi,
) : RemoteMediator<Int, FilmEntity>() {

    private val filmDao = database.filmDao()
    private val remoteKeyDao = database.remoteKeyDao()

    /**
     * Загружает одну порцию в зависимости от [loadType]:
     * - [LoadType.REFRESH] — очищает Room и запрашивает первую порцию (курсор `null`)
     * - [LoadType.APPEND] — берёт курсор из [RemoteKeyEntity] и грузит следующую
     * - [LoadType.PREPEND] — не поддерживается, сразу завершает пагинацию
     *
     * @return [MediatorResult.Success] с флагом `endOfPaginationReached`,
     * если API вернул `next == null`, иначе [MediatorResult.Error]
     * при [IOException] или [HttpException]
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FilmEntity>,
    ): MediatorResult {
        val loadKey: String? = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = remoteKeyDao.getRemoteKey(queryKey)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKey.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val response = feedApi.getMovies(
                next = loadKey,
                limit = state.config.pageSize,
                sortField = sortField,
                sortType = sortType,
            )

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    filmDao.clearAll()
                    remoteKeyDao.deleteByQuery(queryKey)
                }
                remoteKeyDao.insertOrReplace(RemoteKeyEntity(queryKey, response.next))
                filmDao.insertAll(response.docs.mapNotNull { it.toEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
