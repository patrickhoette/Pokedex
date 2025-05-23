plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.patrickhoette.pokemon.presentation"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }

    buildTypes {
        release { isMinifyEnabled = false }
    }

    buildFeatures { compose = true }
}

dependencies {

    // Internal
    api(project(":core:presentation"))
    api(project(":pokemon:domain"))

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(platform(libs.koin.annotation.bom))
    implementation(libs.koin.annotation)
    ksp(libs.koin.annotation.compiler)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.runtime)

    // Immutable Collections
    implementation(libs.kotlin.collections.immutable)

    // Logging
    implementation(libs.napier)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel)

    // Test
    testImplementation(project(":test-android"))
}
