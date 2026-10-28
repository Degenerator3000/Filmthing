package com.degenerator.filmthing.feature.feed

import com.degenerator.filmthing.feature.feed.data.remote.FeedApi
import com.degenerator.filmthing.feature.feed.data.repository.FeedRepositoryImpl
import com.degenerator.filmthing.feature.feed.domain.repository.FeedRepository
import com.degenerator.filmthing.feature.feed.domain.usecase.GetRandomFilmsUseCase
import org.koin.dsl.module
import retrofit2.Retrofit

private val sortFields = listOf(
    "id", "externalId.imdb", "externalId.tmdb", "externalId.kpHD",
    "name", "enName", "alternativeName", "names.name",
    "description", "shortDescription", "slogan", "type",
    "typeNumber", "isSeries", "status", "year",
    "releaseYears.start", "releaseYears.end", "rating.kp", "rating.imdb",
    "rating.tmdb", "rating.filmCritics", "rating.russianFilmCritics",
    "rating.await", "ratingMpaa", "ageRating", "votes.kp",
    "votes.imdb", "votes.tmdb", "votes.filmCritics", "votes.russianFilmCritics",
    "votes.await", "budget.value", "budget.currency", "audience.count",
    "audience.country", "movieLength", "seriesLength", "totalSeriesLength",
    "genres.name", "countries.name", "poster.url", "backdrop.url",
    "logo.url", "ticketsOnSale", "videos.trailers.url", "videos.trailers.site",
    "videos.trailers.name", "networks.items.name", "networks.items.logo.url",
    "persons.id", "persons.name", "persons.enName", "persons.photo",
    "persons.description", "persons.profession", "persons.enProfession",
    "facts.type", "facts.value", "facts.spoiler", "fees.world.value",
    "fees.usa.value", "fees.russia.value", "premiere.world", "premiere.usa",
    "premiere.russia", "premiere.digital", "premiere.dvd", "premiere.bluRay",
    "premiere.cinema", "premiere.country", "similarMovies.id",
    "similarMovies.name", "similarMovies.enName", "similarMovies.alternativeName",
    "similarMovies.poster.url", "similarMovies.rating.kp",
    "similarMovies.rating.imdb", "similarMovies.rating.tmdb",
    "similarMovies.year", "sequelsAndPrequels.id", "sequelsAndPrequels.name",
    "sequelsAndPrequels.enName", "sequelsAndPrequels.alternativeName",
    "sequelsAndPrequels.poster.url", "sequelsAndPrequels.rating.kp",
    "sequelsAndPrequels.rating.imdb", "sequelsAndPrequels.rating.tmdb",
    "sequelsAndPrequels.year", "watchability.items.name",
    "watchability.items.url", "watchability.items.logo.url",
    "lists", "top10", "top250", "updatedAt", "createdAt",
)
private val sortTypes = listOf("1", "-1")

/** Провайдеры фичи feed: FeedApi, FeedRepository, GetRandomFilmsUseCase. */
val feedModule = module {

    single<FeedApi> { get<Retrofit>().create(FeedApi::class.java) }

    single<FeedRepository> { FeedRepositoryImpl(
        database = get(),
        feedApi = get(),
        sortFields = sortFields,
        sortTypes = sortTypes
    ) }

    single { GetRandomFilmsUseCase(feedRepository = get()) }
}
