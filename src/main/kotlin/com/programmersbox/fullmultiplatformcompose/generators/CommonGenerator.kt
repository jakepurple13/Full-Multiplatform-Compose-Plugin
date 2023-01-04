package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.openapi.vfs.VirtualFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.*
import java.io.File

internal const val SHARED_NAME = "SHARED_NAME"
internal const val PACKAGE_NAME = "PACKAGE_NAME"

class CommonGenerator(
    private val params: BuilderParams
) {
    private val androidGenerator = AndroidGenerator(params)
    private val webGenerator = WebGenerator(params)
    private val desktopGenerator = DesktopGenerator(params)

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
            root.build {
                file(
                    "build.gradle.kts",
                    "project_build.gradle.kts",
                    mapOf(
                        PACKAGE_NAME to params.packageName,
                        "APP_NAME" to params.android.appName,
                        "HAS_ANDROID" to params.hasAndroid,
                        "HAS_DESKTOP" to params.hasDesktop,
                        "HAS_IOS" to params.hasiOS,
                        "HAS_WEB" to params.hasWeb,
                    )
                )

                file2(
                    "settings.gradle.kts",
                    "project_settings.gradle.kts",
                    mapOf(
                        PACKAGE_NAME to params.packageName,
                        SHARED_NAME to params.sharedName,
                        "APP_NAME" to params.android.appName.replace(" ", "_"),
                        "HAS_ANDROID" to params.hasAndroid,
                        "HAS_DESKTOP" to params.hasDesktop,
                        "HAS_IOS" to params.hasiOS,
                        "HAS_WEB" to params.hasWeb,
                    )
                )

                file(
                    "gradle.properties",
                    "project_gradle.properties",
                    mapOf()
                )

                dir(params.sharedName) {
                    dir("src") {
                        dir(params.sharedName + "Main") {
                            packageFilesToPlatformKt(
                                packageSegments,
                                "common_platform.kt",
                                mapOf(
                                    SHARED_NAME to params.sharedName,
                                    PACKAGE_NAME to params.packageName,
                                )
                            )
                        }

                        if (params.hasAndroid) {
                            dir("androidMain") {
                                packageFilesToPlatformKt(
                                    packageSegments,
                                    "default_platform.kt",
                                    mapOf(
                                        SHARED_NAME to params.sharedName,
                                        PACKAGE_NAME to params.packageName,
                                        "PLATFORM_TYPE" to "Android"
                                    )
                                ) { dir("resources") }

                                file(
                                    "AndroidManifest.xml",
                                    """
                                    <?xml version="1.0" encoding="utf-8"?>
                                    <manifest xmlns:android="http://schemas.android.com/apk/res/android" package="${params.packageName}.${params.sharedName}"/>
                                    """.trimIndent()
                                )
                            }
                        }

                        if (params.hasDesktop) {
                            dir("desktopMain") {
                                packageFilesToPlatformKt(
                                    packageSegments,
                                    "default_platform.kt",
                                    mapOf(
                                        SHARED_NAME to params.sharedName,
                                        PACKAGE_NAME to params.packageName,
                                        "PLATFORM_TYPE" to "Desktop"
                                    )
                                )
                            }
                        }

                        if (params.hasWeb) {
                            dir("jsMain") {
                                packageFilesToPlatformKt(
                                    packageSegments,
                                    "default_platform.kt",
                                    mapOf(
                                        SHARED_NAME to params.sharedName,
                                        PACKAGE_NAME to params.packageName,
                                        "PLATFORM_TYPE" to "JavaScript"
                                    )
                                )
                            }
                        }

                        if (params.hasiOS) {
                            dir("iosMain") {
                                packageFilesToPlatformKt(
                                    packageSegments,
                                    "default_platform.kt",
                                    mapOf(
                                        SHARED_NAME to params.sharedName,
                                        PACKAGE_NAME to params.packageName,
                                        "PLATFORM_TYPE" to "iOS"
                                    )
                                )
                            }
                        }

                    }

                    file(
                        "build.gradle.kts",
                        "common_build.gradle.kts",
                        mapOf(
                            SHARED_NAME to params.sharedName,
                            PACKAGE_NAME to params.packageName,
                            "MINSDK" to params.android.minimumSdk,
                            "HAS_ANDROID" to params.hasAndroid,
                            "HAS_DESKTOP" to params.hasDesktop,
                            "HAS_IOS" to params.hasiOS,
                            "HAS_WEB" to params.hasWeb,
                        )
                    )
                }

                if (params.hasAndroid) {
                    androidGenerator.generate(this, packageSegments)
                }

                if (params.hasDesktop) {
                    desktopGenerator.generate(this, packageSegments)
                }

                if (params.hasWeb) {
                    webGenerator.generate(this, packageSegments)
                }

            }
            root.refresh(false, true)
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
    }

    private fun File.packageFilesToPlatformKt(
        packageSegments: List<String>,
        templateName: String,
        attributes: Map<String, Any>,
        additions: File.() -> Unit = {}
    ) {
        dir("kotlin") {
            packages(packageSegments) {
                dir(params.sharedName) {
                    file(
                        "platform.kt",
                        templateName,
                        attributes
                    )
                }
            }
            additions()
        }
    }

}