package com.programmersbox.fullmultiplatformcompose

import com.android.tools.idea.run.AndroidRunConfiguration
import com.android.tools.idea.run.AndroidRunConfigurationType
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.JavaRunConfigurationModule
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.idea.run.KotlinRunConfiguration
import org.jetbrains.kotlin.idea.run.KotlinRunConfigurationType
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration

class BuilderConfigurationFactory(private val params: BuilderParams) :
    ConfigurationFactory(GradleExternalTaskConfigurationType.getInstance()) {

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        val conf = GradleRunConfiguration(
            project,
            GradleExternalTaskConfigurationType.getInstance().factory,
            "Run Thing"
        )

        conf.settings.externalProjectPath = project.basePath

        return conf
    }

    override fun getName(): String = "Run Thing"
}

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
}

object RunConfigurationUtil {
    fun createConfigurations(project: Project, params: BuilderParams) {
        val runManager = RunManagerImpl(project)
        val runInstance = RunManagerImpl.getInstanceImpl(project)

        if (params.hasAndroid) {
            runManager.addConfiguration(
                RunnerAndConfigurationSettingsImpl(
                    runInstance,
                    AndroidRunConfiguration(project, AndroidRunConfigurationType.getInstance().factory)
                )
            )
        }

        if (params.hasDesktop) {
            runManager.addConfiguration(
                RunnerAndConfigurationSettingsImpl(
                    runInstance,
                    KotlinRunConfiguration(
                        "MainKt",
                        JavaRunConfigurationModule(project, true),
                        KotlinRunConfigurationType.instance
                    )
                )
            )
        }

        runManager.addConfiguration(
            RunnerAndConfigurationSettingsImpl(
                runInstance,
                BuilderConfigurationFactory(params).createTemplateConfiguration(project)
            )
        )

        if (params.hasWeb) {
            runManager.addConfiguration(
                RunnerAndConfigurationSettingsImpl(
                    runInstance,
                    JsBuilderConfigurationFactory().createTemplateConfiguration(project)
                )
            )
        }

        runManager.setOrder({ one, two -> one.name.compareTo(two.name) })
        runManager.requestSort()
    }
}