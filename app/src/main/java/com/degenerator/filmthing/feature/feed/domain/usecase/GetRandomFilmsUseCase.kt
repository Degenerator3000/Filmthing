package com.degenerator.filmthing.feature.feed.domain.usecase
import androidx.paging.PagingData
import com.degenerator.filmthing.feature.feed.domain.entity.Film
import com.degenerator.filmthing.feature.feed.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow

/**
 * Получение группы случайных фильмов
 */

class GetRandomFilmsUseCase(private val feedRepository: FeedRepository) {
    /**
     * @return @return [Flow] с [PagingData] [Film].
     */
    fun invoke(): Flow<PagingData<Film>> = feedRepository.getRandomFilms()
}
