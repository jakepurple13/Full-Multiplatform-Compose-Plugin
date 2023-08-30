package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderTemplateGroup
import com.programmersbox.fullmultiplatformcompose.utils.GeneratorTemplateFile

class WebGenerator(params: BuilderParams) : PlatformGenerator(params) {
    override fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return listOf(
            GeneratorTemplateFile(
                "jsApp/build.gradle.kts",
                ftManager.getCodeTemplate(BuilderTemplateGroup.JS_BUILD)
            ),
            GeneratorTemplateFile(
                "jsApp/src/jsMain/resources/styles.css"
            ) {
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
            },
            GeneratorTemplateFile(
                "jsApp/src/jsMain/resources/index.html"
            ) {
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
            },
            GeneratorTemplateFile(
                "jsApp/src/jsMain/kotlin/main.js.kt",
                ftManager.getCodeTemplate(BuilderTemplateGroup.JS_MAIN)
            )
        )
    }

    override fun addToCommon(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> = listOf(
        GeneratorTemplateFile(
            "${params.sharedName}/src/jsMain/kotlin/$packageName/${params.sharedName}/platform.kt",
            ftManager.getCodeTemplate(BuilderTemplateGroup.JS_MAIN)
        )
    )

    override fun addRunConfig(ftManager: FileTemplateManager, projectName: String): List<GeneratorAsset> = listOf(
        GeneratorTemplateFile(
            ".run/web.run.xml"
        ) {
            """
            <component name="ProjectRunConfigurationManager">
              <configuration default="false" name="Run Web" type="GradleRunConfiguration" factoryName="Gradle">
                <ExternalSystemSettings>
                  <option name="executionName" />
                  <option name="externalProjectPath" value="${'$'}PROJECT_DIR${'$'}" />
                  <option name="externalSystemIdString" value="GRADLE" />
                  <option name="scriptParameters" value="" />
                  <option name="taskDescriptions">
                    <list />
                  </option>
                  <option name="taskNames">
                    <list>
                      <option value=":jsApp:jsBrowserDevelopmentRun" />
                    </list>
                  </option>
                  <option name="vmOptions" />
                </ExternalSystemSettings>
                <ExternalSystemDebugServerProcess>true</ExternalSystemDebugServerProcess>
                <ExternalSystemReattachDebugProcess>true</ExternalSystemReattachDebugProcess>
                <DebugAllEnabled>false</DebugAllEnabled>
                <method v="2" />
              </configuration>
            </component>
            """.trimIndent()
        }
    )
}