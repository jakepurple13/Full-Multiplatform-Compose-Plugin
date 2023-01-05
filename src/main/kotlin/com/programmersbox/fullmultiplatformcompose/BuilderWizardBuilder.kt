package com.programmersbox.fullmultiplatformcompose

import com.intellij.ide.projectWizard.ProjectSettingsStep
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
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
import com.programmersbox.fullmultiplatformcompose.steps.FirstStep
import com.programmersbox.fullmultiplatformcompose.steps.SecondStep
import com.programmersbox.fullmultiplatformcompose.utils.backgroundTask
import com.programmersbox.fullmultiplatformcompose.utils.runGradle
import java.io.File


class BuilderWizardBuilder : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> = BuilderModuleType()

    val params = BuilderParams()

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val root = createAndGetRoot() ?: return
        modifiableRootModel.addContentEntry(root)
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
                val generator = CommonGenerator(params, modifiableRootModel.project.name)
                generator.generate(root)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            installGradleWrapper(modifiableRootModel.project)
        }
    }

    private fun createAndGetRoot(): VirtualFile? {
        val path = contentEntryPath?.let { FileUtil.toSystemIndependentName(it) } ?: return null
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(File(path).apply { mkdirs() }.absolutePath)
    }

    private fun installGradleWrapper(project: Project) {
        project.runGradle("wrapper --gradle-version 7.6 --distribution-type all")
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep {
        return ProjectSettingsStep(context)
    }

    override fun createWizardSteps(
        wizardContext: WizardContext,
        modulesProvider: ModulesProvider
    ): Array<ModuleWizardStep> {
        return arrayOf(
            FirstStep(this),
            SecondStep(this)
        )
    }

    override fun getIgnoredSteps(): MutableList<Class<out ModuleWizardStep>> {
        return mutableListOf(ProjectSettingsStep::class.java)
    }

    /*override fun modifySettingsStep(settingsStep: SettingsStep): ModuleWizardStep? {
        settingsStep.addSettingsField("HI", JTextField())

        return super.modifySettingsStep(settingsStep)
    }*/

}