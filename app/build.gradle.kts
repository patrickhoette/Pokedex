plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.patrickhoette.pokedex.app"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig {
        applicationId = "com.patrickhoette.pokedex"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
    }

    buildTypes {
        all {
            buildConfigField("String", "ProjectName", "\"${rootProject.name}\"")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Internal
    implementation(project(":database"))

    implementation(project(":core:source"))
    implementation(project(":core:store"))
    implementation(project(":core:ui"))

    implementation(project(":pokemon:source"))
    implementation(project(":pokemon:store"))
    implementation(project(":pokemon:ui"))

    // Android
    implementation(libs.androidx.core.ktx)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.activity)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.navigation)
    implementation(libs.compose.adaptive)
    implementation(libs.compose.adaptive.layout)
    implementation(libs.compose.adaptive.navigation)
    implementation(libs.lifecycle.viewmodel.compose)
    debugImplementation(libs.compose.ui.tooling)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.startup)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)

    implementation(platform(libs.koin.annotation.bom))
    implementation(libs.koin.annotation)
    ksp(libs.koin.annotation.compiler)

    // Logging
    implementation(libs.napier)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Serialization
    implementation(libs.kotlin.serialization.json)

    // Immutable Collections
    implementation(libs.kotlin.collections.immutable)

    // Inspektify
    debugImplementation(libs.inspektify)

    // Ktor
    implementation(libs.ktor)
    implementation(libs.ktor.cio)

    // CoilKt
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    // Squircle
    implementation(libs.squircle)

    // DateTime
    implementation(libs.kotlin.datetime)
}
