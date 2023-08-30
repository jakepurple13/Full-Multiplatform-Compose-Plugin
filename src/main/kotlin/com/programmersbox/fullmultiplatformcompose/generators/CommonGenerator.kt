package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.intellij.ide.starters.local.StarterContext
import com.intellij.openapi.vfs.VirtualFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderTemplateGroup
import com.programmersbox.fullmultiplatformcompose.utils.*
import kotlinx.coroutines.runBlocking

internal const val SHARED_NAME = "SHARED_NAME"
internal const val PACKAGE_NAME = "PACKAGE_NAME"

class CommonGenerator(
    private val params: BuilderParams,
    private val starterContext: StarterContext,
) {
    fun generate(
        list: MutableList<GeneratorAsset>,
        ftManager: FileTemplateManager,
        packageName: String,
    ) = list.apply {
        operator fun GeneratorAsset.unaryPlus() = add(this)

        val generatorList: List<PlatformGenerator2> = listOfNotNull(
            if (params.hasAndroid) AndroidGenerator2(params, starterContext.name) else null,
            if (params.hasDesktop) DesktopGenerator2(params) else null,
            if (params.hasWeb) WebGenerator2(params) else null,
            if (params.hasiOS) IOSGenerator2(params) else null
        )

        //Project
        +GeneratorTemplateFile(
            "build.gradle.kts",
            ftManager.getCodeTemplate(BuilderTemplateGroup.COMPOSE_PROJECT_GRADLE)
        )

        +GeneratorTemplateFile(
            "settings.gradle.kts",
            ftManager.getCodeTemplate(BuilderTemplateGroup.PROJECT_SETTINGS)
        )

        +GeneratorTemplateFile(
            "gradle.properties",
            ftManager.getCodeTemplate(BuilderTemplateGroup.PROJECT_GRADLE)
        )

        //Common
        +GeneratorTemplateFile(
            "${params.sharedName}/src/commonMain/kotlin/$packageName/${params.sharedName}/App.kt",
            ftManager.getCodeTemplate(BuilderTemplateGroup.COMMON_APP)
        )

        +GeneratorTemplateFile(
            "${params.sharedName}/src/commonMain/kotlin/$packageName/${params.sharedName}/platform.kt",
            ftManager.getCodeTemplate(BuilderTemplateGroup.COMMON_PLATFORM)
        )

        +GeneratorTemplateFile(
            "${params.sharedName}/build.gradle.kts",
            ftManager.getCodeTemplate(BuilderTemplateGroup.COMMON_BUILD)
        )

        addAll(generatorList.flatMap { it.commonFiles(ftManager, packageName) })
        addAll(generatorList.flatMap { it.generate(ftManager, packageName) })

        generatorList.forEach { it.setup() }
    }
}

class CommonGenerator2(
    private val params: BuilderParams,
    private val projectName: String,
) {

    private val network = NetworkVersions()

    private fun BuilderParams.hasAndroid() = "HAS_ANDROID" to hasAndroid
    private fun BuilderParams.hasIOS() = "HAS_IOS" to hasiOS
    private fun BuilderParams.hasWeb() = "HAS_WEB" to hasWeb
    private fun BuilderParams.hasDesktop() = "HAS_DESKTOP" to hasDesktop

    fun generate(
        root: VirtualFile,
    ) {
        try {
            val packageSegments = params.packageName.split(".")
            /*groupId
                .split(".")
                .toMutableList()
                .apply { add(artifactId) }
                .toList()*/

            val generatorList = listOfNotNull(
                if (params.hasAndroid) AndroidGenerator(params, projectName) else null,
                if (params.hasDesktop) DesktopGenerator(params) else null,
                if (params.hasWeb) WebGenerator(params) else null,
                if (params.hasiOS) IOSGenerator(params) else null
            )

            root.build {
                file(
                    "build.gradle.kts",
                    "project_build.gradle.kts",
                    mapOf(
                        PACKAGE_NAME to params.packageName,
                        "APP_NAME" to params.android.appName,
                        params.hasAndroid(),
                        params.hasDesktop(),
                        params.hasIOS(),
                        params.hasWeb(),
                    )
                )

                file(
                    "settings.gradle.kts",
                    "project_settings.gradle.kts",
                    mapOf(
                        PACKAGE_NAME to params.packageName,
                        SHARED_NAME to params.sharedName,
                        "APP_NAME" to projectName,
                        params.hasAndroid(),
                        params.hasDesktop(),
                        params.hasIOS(),
                        params.hasWeb(),
                    )
                )

                val versions = runBlocking { network.getVersions(params.remoteVersions) }

                file(
                    "gradle.properties",
                    "project_gradle.properties",
                    mapOf(
                        "COMPOSE" to versions.composeVersion,
                        "KOTLIN" to versions.kotlinVersion,
                        "AGP" to versions.agpVersion,
                        "KTOR" to versions.ktor,
                        "KOIN" to versions.koin
                    )
                )

                dir(params.sharedName) {
                    dir("src") {
                        dir("commonMain") {
                            dir("kotlin") {
                                packages(packageSegments) {
                                    dir(params.sharedName) {
                                        file(
                                            "platform.kt",
                                            "common_platform.kt",
                                            mapOf(
                                                SHARED_NAME to params.sharedName,
                                                PACKAGE_NAME to params.packageName,
                                            )
                                        )

                                        file(
                                            "App.kt",
                                            "common_app.kt",
                                            mapOf(
                                                SHARED_NAME to params.sharedName,
                                                PACKAGE_NAME to params.packageName,
                                                "USE_MATERIAL3" to params.compose.useMaterial3,
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        generatorList.forEach { it.commonFiles(this, packageSegments) }
                    }

                    file(
                        "build.gradle.kts",
                        "common_build.gradle.kts",
                        mapOf(
                            SHARED_NAME to params.sharedName,
                            PACKAGE_NAME to params.packageName,
                            "MINSDK" to params.android.minimumSdk,
                            params.hasAndroid(),
                            params.hasDesktop(),
                            params.hasIOS(),
                            params.hasWeb(),
                            "USE_MATERIAL3" to params.compose.useMaterial3,
                            "androidxAppCompat" to versions.androidxAppCompat,
                            "androidxCore" to versions.androidxCore,
                            "USE_KTOR" to params.library.useKtor,
                            "USE_KOIN" to params.library.useKoin,
                        )
                    )
                }

                generatorList.forEach { it.generate(this, packageSegments) }

                dir(".idea") {
                    arrayOf("gradle.xml").forEach { fileName ->
                        file(
                            fileName,
                            "idea_${fileName}",
                            mapOf(
                                SHARED_NAME to params.sharedName,
                                PACKAGE_NAME to params.packageName,
                                params.hasAndroid(),
                                params.hasDesktop(),
                                params.hasIOS(),
                                params.hasWeb(),
                            )
                        )
                    }
                }

                dir(".run") {
                    generatorList.forEach { it.addRunConfiguration(this, projectName) }
                }
            }
            root.refresh(false, true)
            generatorList.forEach { it.setup(root) }
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
    }
}