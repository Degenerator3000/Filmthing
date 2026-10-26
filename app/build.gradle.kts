import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.serialization)
}

ktlint {
    android.set(true)
    ignoreFailures.set(false) // падать при нарушениях — для CI
}

detekt {
    buildUponDefaultConfig = true // берём дефолтные правила как основу
    allRules = false // не включаем все правила сразу — слишком агрессивно
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    parallel = true // ускоряет анализ
}

android {
    lint {
        // Падать при ошибках (по умолчанию true, но фиксируем явно).
        abortOnError = true

        // В CI: трактовать все warnings как errors.
        // Локально можно поставить false, если шумно.
        warningsAsErrors = true

        // Проверять и зависимости (важно, когда появятся library-модули).
        checkDependencies = true

        lintConfig = file("$rootDir/lint.xml")
        // Baseline — «заморозка» существующих проблем, чтобы проверялись только новые.
        // Для нового проекта не нужен. Включи, когда появится легаси.
        // baseline = file("lint-baseline.xml")

        // Не проверять тестовые исходники на некоторые правила (опционально).
        // checkTestSources = false
    }
    namespace = "com.degenerator.filmthing"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.degenerator.filmthing"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "KP_API_KEY", "\"$kpApiKey\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        jvmTarget = "17"
        html.required.set(true)
        xml.required.set(true)
        sarif.required.set(true)
    }
}

tasks.register("getHomeDir") {
    println("Домашняя папка Gradle: ${gradle.gradleHomeDir}")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}
val kpApiKey: String = localProperties.getProperty("KP_API_KEY") ?: ""
