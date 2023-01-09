package com.programmersbox.fullmultiplatformcompose

import com.android.tools.idea.run.AndroidRunConfiguration
import com.android.tools.idea.run.AndroidRunConfigurationType
import com.intellij.execution.configurations.JavaRunConfigurationModule
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.openapi.project.Project
import com.programmersbox.fullmultiplatformcompose.configurations.JsBuilderConfigurationFactory
import org.jetbrains.kotlin.idea.run.KotlinRunConfiguration
import org.jetbrains.kotlin.idea.run.KotlinRunConfigurationType

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