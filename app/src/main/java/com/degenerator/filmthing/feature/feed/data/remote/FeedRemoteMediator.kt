package com.degenerator.filmthing.feature.feed.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.degenerator.filmthing.core.database.AppDatabase
import com.degenerator.filmthing.feature.feed.data.local.entity.FilmEntity
import com.degenerator.filmthing.feature.feed.data.local.entity.RemoteKeyEntity
import com.degenerator.filmthing.feature.feed.data.mapper.toEntity
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val queryKey: String,
    private val sortField: String,
    private val sortType: String,
    private val database: AppDatabase,
    private val feedApi: FeedApi,
) : RemoteMediator<Int, FilmEntity>() {

    private val filmDao = database.filmDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FilmEntity>,
    ): MediatorResult {
        val loadKey: String? = when (loadType) {
            LoadType.REFRESH -> null // первый запрос — курсор всегда null[reference:6]
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
