plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.patrickhoette.pokemon.store"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }

    buildTypes {
        release { isMinifyEnabled = false }
    }
}

dependencies {

    // Internal
    implementation(project(":database"))
    api(project(":core:store"))
    api(project(":pokemon:data"))

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(platform(libs.koin.annotation.bom))
    implementation(libs.koin.annotation)
    ksp(libs.koin.annotation.compiler)

    // Coroutines
    implementation(libs.coroutines.core)

    // Atomic
    implementation(libs.atomicfu)

    // Logging
    implementation(libs.napier)

    // DateTime
    implementation(libs.kotlin.datetime)

    // Test
    testImplementation(project(":test"))
}
