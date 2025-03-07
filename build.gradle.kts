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

subprojects {
    afterEvaluate {
        tasks.withType(KotlinCompile::class) {
            compilerOptions.freeCompilerArgs.addAll(
                "-Xcontext-receivers",
                "-Xskip-prerelease-check",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            )
        }

        tasks.withType<Test> {
            useJUnitPlatform()
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
