package com.programmersbox.fullmultiplatformcompose

import com.android.tools.idea.welcome.install.AndroidSdk
import com.intellij.ide.projectWizard.ProjectSettingsStep
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.file.PsiDirectoryFactory
import com.programmersbox.fullmultiplatformcompose.generators.CommonGenerator
import com.programmersbox.fullmultiplatformcompose.steps.PlatformOptionsStep
import com.programmersbox.fullmultiplatformcompose.utils.backgroundTask
import com.programmersbox.fullmultiplatformcompose.utils.runGradle
import org.jetbrains.kotlin.idea.projectWizard.WizardStatsService
import org.jetbrains.kotlin.tools.projectWizard.plugins.Plugins
import org.jetbrains.kotlin.tools.projectWizard.plugins.buildSystem.BuildSystemType
import org.jetbrains.kotlin.tools.projectWizard.projectTemplates.ComposeMultiplatformApplicationProjectTemplate
import org.jetbrains.kotlin.tools.projectWizard.wizard.IdeWizard
import org.jetbrains.kotlin.tools.projectWizard.wizard.service.IdeaServices
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.io.File
import javax.swing.JCheckBox


class BuilderWizardBuilder : ModuleBuilder() {
    private val wizard = IdeWizard(Plugins.allPlugins, IdeaServices.PROJECT_INDEPENDENT, isUnitTestMode = false).apply {
        buildSystemType = BuildSystemType.GradleKotlinDsl
        projectTemplate = ComposeMultiplatformApplicationProjectTemplate
    }

    private val uiEditorUsagesStats = WizardStatsService.UiEditorUsageStats()

    val params = BuilderParams()

    override fun getModuleType(): ModuleType<*> = BuilderModuleType()

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val root = createAndGetRoot() ?: return
        modifiableRootModel.addContentEntry(root)
        modifiableRootModel.sdk = moduleJdk
        try {
            ApplicationManager.getApplication().runWriteAction {
                val manager = PsiManager.getInstance(modifiableRootModel.project)
                manager.findFile(root)?.add(
                    PsiDirectoryFactory.getInstance(manager.project)
                        .createDirectory(root.createChildDirectory(null, "webpack"))
                )
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        modifiableRootModel.project.backgroundTask("Setting up project") {
            try {
                CommonGenerator(params, modifiableRootModel.project.name).generate(root)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            installGradleWrapper(modifiableRootModel.project)
            if (params.hasAndroid) {
                AndroidSdk(true)
            }
            if (params.hasiOS) {
                modifiableRootModel.project.runGradle("podInstall")
            }
        }
    }

    private fun createAndGetRoot(): VirtualFile? {
        val path = contentEntryPath?.let { FileUtil.toSystemIndependentName(it) } ?: return null
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(File(path).apply { mkdirs() }.absolutePath)
    }

    private fun installGradleWrapper(project: Project) {
        project.runGradle("wrapper --gradle-version 7.5.1 --distribution-type all")
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep {
        return ProjectSettingsStep(context)
    }

    override fun createWizardSteps(
        wizardContext: WizardContext,
        modulesProvider: ModulesProvider
    ): Array<ModuleWizardStep> {
        return arrayOf(
            PlatformOptionsStep(this),
        )
    }

    override fun modifySettingsStep(settingsStep: SettingsStep): ModuleWizardStep? {
        settingsStep.addCheckboxItem("Include Android", params.hasAndroid) { params.hasAndroid = it }
        if (hostOs == OS.MacOS) {
            settingsStep.addCheckboxItem("Include iOS", params.hasiOS) { params.hasiOS = it }
        }
        settingsStep.addCheckboxItem("Include Desktop", params.hasDesktop) { params.hasDesktop = it }
        settingsStep.addCheckboxItem("Include Web", params.hasWeb) { params.hasWeb = it }

        return super.modifySettingsStep(settingsStep)
    }

    private fun SettingsStep.addCheckboxItem(
        label: String,
        isChecked: Boolean,
        selectedChangeListener: (Boolean) -> Unit
    ) {
        addSettingsField(
            "",
            JCheckBox().apply {
                text = label
                isSelected = isChecked
                addItemListener { selectedChangeListener(!isChecked) }
            }
        )
    }

    override fun getIgnoredSteps(): MutableList<Class<out ModuleWizardStep>> {
        return mutableListOf(ProjectSettingsStep::class.java)
    }

}