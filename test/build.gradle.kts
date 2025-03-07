plugins { alias(libs.plugins.kotlin.jvm) }

dependencies {
    // Test
    api(libs.bundles.test)
    runtimeOnly(libs.junit.jupiter.engine)
}
