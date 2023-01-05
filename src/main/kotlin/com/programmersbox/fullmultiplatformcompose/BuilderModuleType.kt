package com.programmersbox.fullmultiplatformcompose

import com.intellij.icons.AllIcons
import com.intellij.openapi.module.ModuleType
import javax.swing.Icon

class BuilderModuleType : ModuleType<BuilderWizardBuilder>(ID) {

    companion object {
        const val ID: String = "FULL_MULTIPLATFORM_COMPOSE_WIZARD"
    }

    override fun createModuleBuilder(): BuilderWizardBuilder = BuilderWizardBuilder()

    override fun getName(): String = "Multiplatform Compose"

    override fun getDescription(): String = "Hello"

    override fun getNodeIcon(isOpened: Boolean): Icon = AllIcons.Nodes.Module

}