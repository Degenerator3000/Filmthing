package com.degenerator.filmthing.core.network
import com.degenerator.filmthing.BuildConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "https://api.kinopoisk.dev/"
private const val API_KEY_HEADER = "X-API-KEY"

/**
 * Создаёт singleton [OkHttpClient] с двумя интерсепторами:
 * - добавляет `X-API-KEY` ко всем запросам
 * - логирует тела запросов/ответов
 */
fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header(API_KEY_HEADER, BuildConfig.KP_API_KEY)
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(loggingInterceptor)
        .build()
}

/**
 * Создаёт singleton [Retrofit] с конвертером `kotlinx.serialization`.
 * [JsonBuilder.ignoreUnknownKeys] включён
 */
fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val json = Json { ignoreUnknownKeys = true }
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
