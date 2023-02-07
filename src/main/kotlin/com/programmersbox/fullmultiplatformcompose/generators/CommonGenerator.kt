package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.openapi.vfs.VirtualFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.*
import kotlinx.coroutines.runBlocking

internal const val SHARED_NAME = "SHARED_NAME"
internal const val PACKAGE_NAME = "PACKAGE_NAME"

class CommonGenerator(
    private val params: BuilderParams,
    private val projectName: String
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
                if (params.hasAndroid) AndroidGenerator(params) else null,
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
                            "androidxCore" to versions.androidxCore
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