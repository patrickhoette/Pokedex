plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

dependencies {

    // Internal
    api(project(":core:domain"))

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(platform(libs.koin.annotation.bom))
    implementation(libs.koin.annotation)
    ksp(libs.koin.annotation.compiler)

    // Coroutines
    implementation(libs.coroutines.core)

    // Test
    testImplementation(project(":test"))
}
