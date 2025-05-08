plugins { alias(libs.plugins.kotlin.jvm) }

dependencies {
    implementation(project(":entity"))
    implementation(project(":core:utils"))

    // Test
    api(libs.bundles.test)
    runtimeOnly(libs.junit.jupiter.engine)

    // Reflection
    api(kotlin("reflect"))

    // DateTime
    implementation(libs.kotlin.datetime)
}
