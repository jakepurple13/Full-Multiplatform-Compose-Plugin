package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.intellij.ide.starters.local.StarterContext
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderTemplateGroup

internal const val SHARED_NAME = "SHARED_NAME"
internal const val PACKAGE_NAME = "PACKAGE_NAME"

class CommonGenerator(
    private val params: BuilderParams,
    private val starterContext: StarterContext,
) {
    fun generate(
        list: MutableList<GeneratorAsset>,
        ftManager: FileTemplateManager,
        packageName: String,
    ) = list.apply {
        operator fun GeneratorAsset.unaryPlus() = add(this)

        val generatorList: List<PlatformGenerator> = listOfNotNull(
            if (params.hasAndroid) AndroidGenerator(params, starterContext.artifact) else null,
            if (params.hasDesktop) DesktopGenerator(params) else null,
            if (params.hasWeb) WebGenerator(params) else null,
            if (params.hasiOS) IOSGenerator(params) else null
        )

        //Project
        +GeneratorTemplateFile(
            "build.gradle.kts",
            ftManager.getCodeTemplate(BuilderTemplateGroup.COMPOSE_PROJECT_GRADLE)
        )

        +GeneratorTemplateFile(
            "settings.gradle.kts",
            ftManager.getCodeTemplate(BuilderTemplateGroup.PROJECT_SETTINGS)
        )

        +GeneratorTemplateFile(
            "gradle.properties",
            ftManager.getCodeTemplate(BuilderTemplateGroup.PROJECT_GRADLE)
        )

        +GeneratorTemplateFile(
            "gradle/libs.versions.toml",
            ftManager.getCodeTemplate(BuilderTemplateGroup.PROJECT_TOML)
        )

        //Common
        +GeneratorTemplateFile(
            "${params.sharedName}/src/commonMain/kotlin/$packageName/${params.sharedName}/App.kt",
            ftManager.getCodeTemplate(BuilderTemplateGroup.COMMON_APP)
        )

        +GeneratorTemplateFile(
            "${params.sharedName}/src/commonMain/kotlin/$packageName/${params.sharedName}/platform.kt",
            ftManager.getCodeTemplate(BuilderTemplateGroup.COMMON_PLATFORM)
        )

        +GeneratorTemplateFile(
            "${params.sharedName}/build.gradle.kts",
            ftManager.getCodeTemplate(BuilderTemplateGroup.COMMON_BUILD)
        )

        addAll(generatorList.flatMap { it.commonFiles(ftManager, packageName) })
        addAll(generatorList.flatMap { it.generate(ftManager, packageName) })
        addAll(generatorList.flatMap { it.addRunConfiguration(ftManager, starterContext.artifact) })

        generatorList.forEach { it.setup() }
    }
}