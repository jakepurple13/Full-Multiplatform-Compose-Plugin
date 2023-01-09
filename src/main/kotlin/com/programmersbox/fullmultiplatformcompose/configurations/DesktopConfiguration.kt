package com.programmersbox.fullmultiplatformcompose.configurations

import com.intellij.execution.Executor
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.*
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.StoredProperty
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.run.*
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class DesktopBuilderRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?) :
    RunConfigurationBase<DesktopRunConfigurationOptions>(project, factory, name) {

    var scriptName
        get() = (options as DesktopRunConfigurationOptions).scriptName
        set(value) {
            (options as DesktopRunConfigurationOptions).scriptName = value
        }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return object : JavaCommandLineState(environment) {
            override fun createJavaParameters(): JavaParameters = JavaParameters()

        }
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return DesktopSettingsEditor()
    }

    override fun getOptions(): RunConfigurationOptions {
        return super.getOptions() as DesktopRunConfigurationOptions
    }

}

class DesktopRunConfigurationOptions : RunConfigurationOptions() {
    private val myScriptName: StoredProperty<String?> = string("Run Desktop")
        .provideDelegate(this, "scriptName")
    var scriptName: String?
        get() = myScriptName.getValue(this)
        set(scriptName) {
            myScriptName.setValue(this, scriptName)
        }
}

class DesktopSettingsEditor : SettingsEditor<DesktopBuilderRunConfiguration>() {
    private val myPanel: JPanel? = null
    private var myScriptName: LabeledComponent<TextFieldWithBrowseButton>? = null
    override fun resetEditorFrom(demoRunConfiguration: DesktopBuilderRunConfiguration) {
        myScriptName?.component?.setText(demoRunConfiguration.scriptName)
    }

    override fun applyEditorTo(demoRunConfiguration: DesktopBuilderRunConfiguration) {
        demoRunConfiguration.scriptName = myScriptName?.component?.text
    }


    override fun createEditor(): JComponent {
        return myPanel!!
    }

    private fun createUIComponents() {
        myScriptName = LabeledComponent()
        myScriptName!!.component = TextFieldWithBrowseButton()
    }
}

class DesktopConfigurationFactory(type: ConfigurationType?) : ConfigurationFactory(type!!) {

    override fun getId(): String {
        return DesktopRunConfigurationType.ID
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return DesktopBuilderRunConfiguration(project, this, "Desktop")
    }

    override fun getOptionsClass(): Class<out BaseState> {
        return DesktopRunConfigurationOptions::class.java
    }
}

class DesktopRunConfigurationType : ConfigurationType {

    override fun getDisplayName(): String {
        return "Run Desktop"
    }

    override fun getConfigurationTypeDescription(): String {
        return "Demo run configuration type"
    }

    override fun getIcon(): Icon {
        return AllIcons.General.Information
    }

    override fun getId(): String {
        return ID
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(DesktopConfigurationFactory(this))
    }

    companion object {
        const val ID = "DesktopRunConfiguration"
    }
}

class DesktopConfigSetup : LazyRunConfigurationProducer<KotlinRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory {
        return DesktopConfigurationFactory(KotlinRunConfigurationType())
    }

    override fun setupConfigurationFromContext(
        configuration: KotlinRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val location = context.location ?: return false
        val module = location.module?.asJvmModule() ?: return false
        context.runManager.addConfiguration(
            RunnerAndConfigurationSettingsImpl(
                RunManagerImpl(context.project),
                KotlinRunConfiguration(
                    "Desktop",
                    JavaRunConfigurationModule(context.project, true),
                    KotlinRunConfigurationType.instance
                ).apply {
                    runClass = "MainKt"
                    setModule(module)
                    setGeneratedName()
                }
            )
        )
        return true
    }

    override fun isConfigurationFromContext(
        configuration: KotlinRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val entryPointContainer =
            context.location?.psiElement?.let { EntryPointContainerFinder.find(it) } ?: return false
        val startClassFQName = KotlinRunConfigurationProducer.getStartClassFqName(entryPointContainer) ?: return false

        return configuration.runClass == startClassFQName &&
                context.module?.asJvmModule() == configuration.configurationModule?.module
    }

}