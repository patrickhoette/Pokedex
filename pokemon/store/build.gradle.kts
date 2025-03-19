plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "com.patrickhoette.pokemon.store"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }

    buildTypes {
        release { isMinifyEnabled = false }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName = "com.patrickhoette.pokemon.store.database"
            generateAsync = true
            verifyMigrations = true
        }
    }
}

dependencies {

    // Internal
    implementation(project(":entity"))
    implementation(project(":core:store"))
    implementation(project(":core:utils"))
    implementation(project(":pokemon:data"))

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

    // SQLDelight
    implementation(libs.sqldelight.coroutines)

    // Atomic
    implementation(libs.atomicfu)

    // Logging
    implementation(libs.napier)

    // Test
    testImplementation(project(":test"))
}
