
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.google.devtools.ksp.gradle.KspExtension
import com.google.devtools.ksp.gradle.KspGradleSubplugin
import dev.iurysouza.modulegraph.Theme
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.modulegraph)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.mannodermaus) apply false
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

        // if (project.extensions.findByType(BaseExtension::class.java) != null) {
        //     plugins.apply(libs.plugins.mannodermaus.get().pluginId)
        // }
    }
}

val moduleGraphReportFile = layout.buildDirectory.file("reports/project-connections/graph.md")

val moduleGraphTask = tasks.createModuleGraph
val createModuleGraphReportFileTask = tasks.register("createModuleGraphReportFile") {
    val outputFile = moduleGraphReportFile
    outputs.file(outputFile)

    doLast {
        val file = outputFile.get().asFile
        file.ensureParentDirsCreated()
        file.createNewFile()
    }
}

val editModuleGraphRendererTask = tasks.register("editModuleGraphRenderer") {
    val graphFile = moduleGraphReportFile
    outputs.file(graphFile)

    doLast {
        val file = graphFile.get().asFile
        val contents = file.readLines().joinToString("\n")
        val newContents = contents.replace(
            """
            %%{
              init: {
                'theme': 'dark'
              }
            }%%
            """.trimIndent(),
            """
            %%{
              init: {
                'theme': 'dark',
                "flowchart": { "defaultRenderer": "elk"}
              }
            }%%
            """.trimIndent()
        )
        file.writeText(newContents)
    }
}

moduleGraphTask.dependsOn(createModuleGraphReportFileTask)
editModuleGraphRendererTask.dependsOn(moduleGraphTask)
moduleGraphTask.configure { finalizedBy(editModuleGraphRendererTask) }

moduleGraphConfig {
    setStyleByModuleType = true

    val excludeModules = ":test|:entity|:test-android|:app"
    rootModulesRegex = "^(?!(?:$excludeModules)\$).+\$"
    excludedModulesRegex = "($excludeModules)"
    readmePath = moduleGraphReportFile.map { it.asFile.absolutePath }
    heading = ""
    theme = Theme.DARK
}

val listAllDependenciesTask = tasks.register("listAllDependencies")

allprojects {
    afterEvaluate {
        listAllDependenciesTask.dependsOn(tasks.dependencies)
    }
}
