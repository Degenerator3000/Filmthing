package com.degenerator.filmthing

import org.junit.jupiter.api.Test
import org.koin.dsl.module
import org.koin.test.verify.verify
import android.content.Context
import com.degenerator.filmthing.core.di.databaseModule
import com.degenerator.filmthing.core.di.networkModule
import com.degenerator.filmthing.feature.feed.feedModule


class KoinModulesTest {

    @Test
    fun `verify all modules`() {
        val allModules = module {
            includes(networkModule, databaseModule, feedModule)
        }

        allModules.verify(
            extraTypes = listOf(Context::class)
        )
    }
}
