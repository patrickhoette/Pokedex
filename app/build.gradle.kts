plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
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
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName = "com.patrickhoette.pokedex.app.database"
            generateAsync = true
            verifyMigrations = true

            dependency(project(":pokemon:store"))
        }
    }
}

dependencies {

    // Internal
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:presentation"))
    implementation(project(":core:source"))
    implementation(project(":core:store"))
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))
    
    implementation(project(":entity"))

    implementation(project(":pokemon:data"))
    implementation(project(":pokemon:domain"))
    implementation(project(":pokemon:presentation"))
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

    // SQLDelight
    implementation(libs.sqldelight.coroutines)
    implementation(libs.sqldelight.android)
}
