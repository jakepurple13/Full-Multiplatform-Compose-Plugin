package com.programmersbox.fullmultiplatformcompose.generators

import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.dir
import com.programmersbox.fullmultiplatformcompose.utils.file
import com.programmersbox.fullmultiplatformcompose.utils.packages
import java.io.File

class AndroidGenerator(params: BuilderParams) : PlatformGenerator(params) {
    override fun File.generateProject(packageSegments: List<String>) {
        dir("android") {
            dir("src") {
                dir("main") {
                    dir("java") {
                        packages(packageSegments) {
                            dir("android") {
                                file(
                                    "MainActivity.kt",
                                    "android_mainactivity.kt",
                                    mapOf(
                                        SHARED_NAME to params.sharedName,
                                        PACKAGE_NAME to params.packageName,
                                        "USE_MATERIAL3" to params.compose.useMaterial3,
                                    )
                                )
                            }
                        }
                        dir("res")
                    }
                    file(
                        "AndroidManifest.xml",
                        "android_manifest.xml",
                        mapOf(
                            PACKAGE_NAME to params.packageName,
                            "APP_NAME" to params.android.appName
                        )
                    )
                }
            }
            file(
                "build.gradle.kts",
                "android_build.gradle.kts",
                mapOf(
                    SHARED_NAME to params.sharedName,
                    PACKAGE_NAME to params.packageName,
                    "MINSDK" to params.android.minimumSdk
                )
            )
        }
    }

    override fun File.addToCommon(packageSegments: List<String>) {
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
}