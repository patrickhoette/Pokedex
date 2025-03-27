plugins { alias(libs.plugins.kotlin.jvm) }

dependencies {
    implementation(project(":core:utils"))

    // Test
    api(libs.bundles.test)
    runtimeOnly(libs.junit.jupiter.engine)

    // DateTime
    implementation(libs.kotlin.datetime)
}
