package com.programmersbox.fullmultiplatformcompose.generators

import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.dir
import com.programmersbox.fullmultiplatformcompose.utils.file
import java.io.File

class DesktopGenerator(params: BuilderParams) : PlatformGenerator(params) {
    override fun File.generateProject(packageSegments: List<String>) {
        dir("desktop") {
            dir("src") {
                dir("jvmMain") {
                    dir("kotlin") {
                        file(
                            "Main.kt",
                            "desktop_main.kt",
                            mapOf(
                                SHARED_NAME to params.sharedName,
                                PACKAGE_NAME to params.packageName,
                            )
                        )
                    }
                    dir("resources")
                }
            }
            file(
                "build.gradle.kts",
                "desktop_build.gradle.kts",
                mapOf(
                    SHARED_NAME to params.sharedName,
                    PACKAGE_NAME to params.packageName,
                    "APP_NAME" to params.android.appName
                )
            )
        }
    }
}