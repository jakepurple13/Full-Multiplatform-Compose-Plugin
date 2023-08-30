package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderTemplateGroup
import com.programmersbox.fullmultiplatformcompose.utils.GeneratorTemplateFile

class DesktopGenerator(params: BuilderParams) : PlatformGenerator(params) {
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
                "${params.sharedName}/src/desktopMain/kotlin/$packageName/${params.sharedName}/platform.kt",
                ftManager.getCodeTemplate(BuilderTemplateGroup.DEFAULT_PLATFORM)
            )
        )
    }

    override fun addRunConfig(ftManager: FileTemplateManager, projectName: String): List<GeneratorAsset> = listOf(
        GeneratorTemplateFile(
            ".run/desktop.run.xml"
        ) {
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
    )
}