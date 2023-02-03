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
                                "APP_NAME" to params.android.appName,
                                "USE_MATERIAL3" to params.compose.useMaterial3,
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
                                    <title>compose multiplatform web</title>
                                    <script src="skiko.js"> </script>
                                    <link type="text/css" rel="stylesheet" href="styles.css" />
                                </head>
                                <body>
                                <div>
                                    <canvas id="ComposeTarget"></canvas>
                                </div>
                                <script src="jsApp.js"> </script>
                                <script>
                                (function() {
                                  var
                                    // Obtain a reference to the canvas element using its id.
                                    htmlCanvas = document.getElementById('ComposeTarget');

                                  // Start listening to resize events and draw canvas.
                                  initialize();

                                  function initialize() {
                                    // Register an event listener to call the resizeCanvas() function
                                    // each time the window is resized.
                                    window.addEventListener('resize', resizeCanvas, false);
                                    // Draw canvas border for the first time.
                                    resizeCanvas();
                                  }

                                  // Runs each time the DOM window resize event fires.
                                  // Resets the canvas dimensions to match window,
                                  // then draws the new borders accordingly.
                                  function resizeCanvas() {
                                    htmlCanvas.width = window.innerWidth;
                                    htmlCanvas.height = window.innerHeight;
                                  }
                                })();
                                </script>
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
                                
                                canvas {
                                    width: 90%;
                                    height: 90%;
                                    display: block;
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

    override fun File.addToCommon(packageSegments: List<String>) {
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
}