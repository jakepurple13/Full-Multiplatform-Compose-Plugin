package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderTemplateGroup
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
                                "USE_MATERIAL3" to params.compose.useMaterial3,
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

    override fun File.addToCommon(packageSegments: List<String>) {
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

    override fun File.addRunConfig(projectName: String) {
        file("Run_Desktop.run.xml") {
            """
            <component name="ProjectRunConfigurationManager">
              <configuration default="false" name="MainKt" type="JetRunConfigurationType" nameIsGenerated="true">
                <option name="MAIN_CLASS_NAME" value="MainKt" />
                <module name="$projectName.desktop.jvmMain" />
                <shortenClasspath name="NONE" />
                <method v="2">
                  <option name="Make" enabled="true" />
                </method>
              </configuration>
            </component>
            """.trimIndent()
        }
    }
}

class DesktopGenerator2(params: BuilderParams) : PlatformGenerator2(params) {
    override fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return listOf(
            GeneratorTemplateFile(
                "desktop/src/jvmMain/kotlin/Main.kt",
                ftManager.getCodeTemplate(BuilderTemplateGroup.DESKTOP_MAIN)
            ),
            GeneratorEmptyDirectory("desktop/src/jvmMain/resources"),
            GeneratorTemplateFile(
                "desktop/build.gradle.kts",
                ftManager.getCodeTemplate(BuilderTemplateGroup.DESKTOP_BUILD)
            )
        )
    }

    override fun addToCommon(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return listOf(
            GeneratorTemplateFile(
                "${params.sharedName}/src/desktopMain/$packageName/${params.sharedName}/platform.kt",
                ftManager.getCodeTemplate(BuilderTemplateGroup.DEFAULT_PLATFORM)
            )
        )
    }
}