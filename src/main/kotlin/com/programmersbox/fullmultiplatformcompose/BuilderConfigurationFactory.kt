package com.programmersbox.fullmultiplatformcompose

import com.android.tools.idea.run.AndroidRunConfiguration
import com.android.tools.idea.run.AndroidRunConfigurationType
import com.intellij.execution.configurations.JavaRunConfigurationModule
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.programmersbox.fullmultiplatformcompose.configurations.JsBuilderConfigurationFactory
import org.jetbrains.kotlin.idea.run.KotlinRunConfiguration
import org.jetbrains.kotlin.idea.run.KotlinRunConfigurationType
import org.jetbrains.kotlin.idea.run.asJvmModule

object RunConfigurationUtil {
    fun createConfigurations(project: Project, params: BuilderParams) {
        val runInstance = RunManagerImpl.getInstanceImpl(project)

        val android = if (params.hasAndroid) {
            println("Creating Android")
            RunnerAndConfigurationSettingsImpl(
                runInstance,
                AndroidRunConfiguration(project, AndroidRunConfigurationType.getInstance().factory)
            )
        } else null

        android?.let(runInstance::addConfiguration)

        val desktop = if (params.hasDesktop) {
            println("Creating Desktop")
            runWriteAction {
                RunnerAndConfigurationSettingsImpl(
                    runInstance,
                    KotlinRunConfiguration(
                        "Desktop",
                        JavaRunConfigurationModule(project, true),
                        KotlinRunConfigurationType.instance
                    ).apply {
                        val module = ModuleManager.getInstance(project)
                            .findModuleByName("desktop")
                            ?.asJvmModule()
                        println(module)

                        runClass = "MainKt"
                        setModule(module)
                        setGeneratedName()
                    }
                )
            }
        } else null

        desktop?.let(runInstance::addConfiguration)

        val web = if (params.hasWeb) {
            println("Creating Web")
            RunnerAndConfigurationSettingsImpl(
                runInstance,
                JsBuilderConfigurationFactory().createTemplateConfiguration(project)
            )
        } else null

        web?.let(runInstance::addConfiguration)

        runInstance.selectedConfiguration = when {
            params.hasDesktop -> desktop
            params.hasAndroid -> android
            params.hasWeb -> web
            else -> null
        }
        runInstance.setOrder({ one, two -> one.name.compareTo(two.name) })
        runInstance.requestSort()
    }
}