package com.programmersbox.fullmultiplatformcompose.configurations
/*

import com.android.tools.idea.run.AndroidRunConfiguration
import com.android.tools.idea.run.AndroidRunConfigurationType
import com.intellij.openapi.project.Project
import com.programmersbox.fullmultiplatformcompose.BuilderParams

object BuilderConfigurationFactory {
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

        */
/*val desktop = if (params.hasDesktop) {
            println("Creating Desktop")
            RunnerAndConfigurationSettingsImpl(
                runInstance,
                KotlinRunConfiguration(
                    "Desktop12",
                    JavaRunConfigurationModule(project, true),
                    KotlinRunConfigurationType.instance
                ).apply {
                    val f = project.projectFile
                        ?.children
                        .also { it?.forEach { println(it) } }
                        ?.find { it.name == "desktop" }
                        .also { println(it) }
                        ?.getModule(project)?.asJvmModule()
                    println(f)
                    println(modifiableRootModel.module)
                    println(modifiableRootModel.module.asJvmModule())
                    project.allModules().forEach { println(it) }
                    val mp = ModuleManager.getInstance(project)
                    mp.modules.forEach { println(it) }
                    val m = mp.findModuleByName(project.name)?.asJvmModule()
                    println(m)
                    val module = mp
                        .findModuleByName("${project.name}.desktop.jvmMain")
                        ?.asJvmModule()
                    println(module)

                    runClass = "MainKt"
                    setModule(module)
                    setGeneratedName()
                }
            )
        } else null

        desktop?.let(runInstance::addConfiguration)*//*


        val web = if (params.hasWeb) {
            println("Creating Web")
            RunnerAndConfigurationSettingsImpl(
                runInstance,
                JsBuilderConfigurationFactory().createTemplateConfiguration(project)
            )
        } else null

        web?.let(runInstance::addConfiguration)

        runInstance.selectedConfiguration = when {
            params.hasAndroid -> android
            //params.hasDesktop -> desktop
            params.hasWeb -> web
            else -> null
        }
        runInstance.setOrder({ one, two -> one.name.compareTo(two.name) })
        runInstance.requestSort()
    }
}*/
