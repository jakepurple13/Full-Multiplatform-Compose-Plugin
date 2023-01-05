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
                            file(
                                "MainActivity.kt",
                                "android_mainactivity.kt",
                                mapOf(
                                    SHARED_NAME to params.sharedName,
                                    PACKAGE_NAME to params.packageName,
                                )
                            )
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
}