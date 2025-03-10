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
}

allprojects {
    val displayName = displayName
        .removePrefix("project ':")
        .removeSuffix("'")
        .replace(':', '-')
    tasks.create("printName") {
        doLast {
            logger.quiet("displayName='$displayName'")
        }
    }

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
