package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.programmersbox.fullmultiplatformcompose.BuilderParams

abstract class PlatformGenerator(protected val params: BuilderParams) {

    open fun setup() {}

    fun generate(
        ftManager: FileTemplateManager,
        packageName: String,
    ) = generateProject(ftManager, packageName)

    protected abstract fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset>

    fun commonFiles(
        ftManager: FileTemplateManager,
        packageName: String,
    ) = addToCommon(ftManager, packageName)

    protected abstract fun addToCommon(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset>

    fun addRunConfiguration(ftManager: FileTemplateManager, projectName: String) = addRunConfig(ftManager, projectName)

    protected open fun addRunConfig(ftManager: FileTemplateManager, projectName: String) = emptyList<GeneratorAsset>()

}