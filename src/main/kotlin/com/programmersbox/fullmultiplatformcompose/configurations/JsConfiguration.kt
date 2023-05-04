package com.programmersbox.fullmultiplatformcompose.configurations

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.StoredProperty
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class JsBuilderConfigurationFactory : ConfigurationFactory(GradleExternalTaskConfigurationType.getInstance()) {

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        val conf = GradleRunConfiguration(
            project,
            GradleExternalTaskConfigurationType.getInstance().factory,
            "Run Web"
        )

        conf.settings.externalProjectPath = project.basePath
        conf.settings.taskNames = listOf(":jsApp:jsBrowserDevelopmentRun")
        return conf
    }

    override fun getName(): String = "Run Web"

    override fun getId(): String = JsRunConfigurationType.ID
}

class JsBuilderRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?) :
    RunConfigurationBase<JsRunConfigurationOptions>(project, factory, name) {

    var scriptName
        get() = (options as JsRunConfigurationOptions).scriptName
        set(value) {
            (options as JsRunConfigurationOptions).scriptName = value
        }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return object : CommandLineState(environment) {
            @Throws(ExecutionException::class)
            override fun startProcess(): ProcessHandler {
                val commandLine = GeneralCommandLine("./gradlew", ":jsApp:jsBrowserDevelopmentRun")
                val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
                ProcessTerminatedListener.attach(processHandler)
                return processHandler
            }
        }
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return JsSettingsEditor()
    }

    override fun getOptions(): RunConfigurationOptions {
        return super.getOptions() as JsRunConfigurationOptions
    }

}

class JsRunConfigurationOptions : RunConfigurationOptions() {
    private val myScriptName: StoredProperty<String?> = string("Run Web")
        .provideDelegate(this, "scriptName")
    var scriptName: String?
        get() = myScriptName.getValue(this)
        set(scriptName) {
            myScriptName.setValue(this, scriptName)
        }
}

class JsSettingsEditor : SettingsEditor<JsBuilderRunConfiguration>() {
    private val myPanel: JPanel? = null
    private var myScriptName: LabeledComponent<TextFieldWithBrowseButton>? = null
    override fun resetEditorFrom(demoRunConfiguration: JsBuilderRunConfiguration) {
        myScriptName?.component?.setText(demoRunConfiguration.scriptName)
    }

    override fun applyEditorTo(demoRunConfiguration: JsBuilderRunConfiguration) {
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

class JsConfigurationFactory(type: ConfigurationType?) : ConfigurationFactory(type!!) {

    override fun getId(): String {
        return JsRunConfigurationType.ID
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return JsBuilderRunConfiguration(project, this, "Web")
    }

    override fun getOptionsClass(): Class<out BaseState> {
        return JsRunConfigurationOptions::class.java
    }
}

class JsRunConfigurationType : ConfigurationType {

    override fun getDisplayName(): String {
        return "Run Web"
    }

    override fun getConfigurationTypeDescription(): String {
        return "Web run configuration type"
    }

    override fun getIcon(): Icon {
        return AllIcons.General.Web
    }

    override fun getId(): String {
        return ID
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(JsConfigurationFactory(this))
    }

    companion object {
        const val ID = "JsRunConfiguration"
    }
}