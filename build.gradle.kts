
import com.google.devtools.ksp.gradle.KspExtension
import com.google.devtools.ksp.gradle.KspGradleSubplugin
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.modulegraph)
}

allprojects {
    beforeEvaluate {
        version = libs.versions.version.name.get()
    }
}

subprojects {
    afterEvaluate {
        val moduleName = displayName
            .removePrefix("project ':")
            .removeSuffix("'")
            .replace(':', '-')

        tasks.withType(KotlinCompile::class) {
            compilerOptions.freeCompilerArgs.addAll(
                "-Xcontext-receivers",
                "-Xskip-prerelease-check",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-module-name=$moduleName"
            )
        }

        tasks.withType<Test> {
            useJUnitPlatform()
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
        }

        extensions.findByType<KotlinAndroidProjectExtension>()
            ?.jvmToolchain(libs.versions.jvm.get().toInt())
            ?: extensions.findByType<KotlinProjectExtension>()
                ?.jvmToolchain(libs.versions.jvm.get().toInt())

        plugins.withType<KspGradleSubplugin> {
            extensions.configure<KspExtension> {
                arg("KOIN_DEFAULT_MODULE", "false")
            }
        }
    }
}

moduleGraphConfig {
    setStyleByModuleType = true

    val excludeModules = ":test|:entity|:test-android|:app"
    rootModulesRegex = "^(?!(?:$excludeModules)\$).+\$"
    excludedModulesRegex = "($excludeModules)"
    readmePath = layout.buildDirectory.file("reports/project-connections/graph.md").map { it.asFile.absolutePath }
    heading = ""
}
