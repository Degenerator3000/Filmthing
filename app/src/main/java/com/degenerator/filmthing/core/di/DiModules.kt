package com.degenerator.filmthing.core.di

import androidx.room.Room
import com.degenerator.filmthing.core.database.AppDatabase
import com.degenerator.filmthing.core.network.provideOkHttpClient
import com.degenerator.filmthing.core.network.provideRetrofit
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Провайдеры сетевого модуля: OkHttpClient, Retrofit. */
val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
}

/** Провайдеры Room: AppDatabase, filmDao, remoteKeyDao. */
val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "filmthing.db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    single { get<AppDatabase>().filmDao() }
    single { get<AppDatabase>().remoteKeyDao() }
}
