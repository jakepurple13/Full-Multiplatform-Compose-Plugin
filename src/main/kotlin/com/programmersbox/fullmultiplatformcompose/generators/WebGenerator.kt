package com.programmersbox.fullmultiplatformcompose.generators

import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.dir
import com.programmersbox.fullmultiplatformcompose.utils.file
import java.io.File

class WebGenerator(params: BuilderParams) : PlatformGenerator(params) {
    override fun File.generateProject(packageSegments: List<String>) {
        dir("jsApp") {
            dir("src") {
                dir("jsMain") {
                    dir("kotlin") {
                        file(
                            "main.js.kt",
                            "js_main.js.kt",
                            mapOf(
                                SHARED_NAME to params.sharedName,
                                PACKAGE_NAME to params.packageName,
                                "APP_NAME" to params.android.appName
                            )
                        )
                    }
                    dir("resources") {
                        file(
                            "index.html",
                            """
                                <!DOCTYPE html>
                                <html lang="en">
                                <head>
                                    <meta charset="UTF-8">
                                    <title>compose multiplatform web demo</title>
                                    <script src="skiko.js"> </script>
                                    <link type="text/css" rel="stylesheet" href="styles.css" />
                                </head>
                                <body>
                                <h1>compose multiplatform web demo</h1>
                                <div>
                                    <canvas id="ComposeTarget" width="800" height="600"></canvas>
                                </div>
                                <script src="jsApp.js"> </script>
                                </body>
                                </html>
                            """.trimIndent()
                        )
                        file(
                            "styles.css",
                            """
                                #root {
                                    width: 100%;
                                    height: 100vh;
                                }

                                #root > .compose-web-column > div {
                                    position: relative;
                                }
                            """.trimIndent()
                        )
                    }
                }
            }

            file(
                "build.gradle.kts",
                "js_build.gradle.kts",
                mapOf(
                    SHARED_NAME to params.sharedName,
                )
            )
        }
    }
}