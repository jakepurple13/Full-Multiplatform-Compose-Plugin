package com.programmersbox.fullmultiplatformcompose

import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class BuilderModuleType : ModuleType<BuilderWizardBuilder>(ID) {
    private val _icon: Icon by lazy { IconLoader.getIcon("/pluginicon.svg", BuilderModuleType::class.java) }

    companion object {
        val ID: String = "FULL_MULTIPLATFORM_COMPOSE_WIZARD"
    }

    override fun createModuleBuilder(): BuilderWizardBuilder = BuilderWizardBuilder()

    override fun getName(): String = "Multiplatform Compose"

    override fun getDescription(): String = "Hello"

    override fun getNodeIcon(isOpened: Boolean): Icon = _icon

}