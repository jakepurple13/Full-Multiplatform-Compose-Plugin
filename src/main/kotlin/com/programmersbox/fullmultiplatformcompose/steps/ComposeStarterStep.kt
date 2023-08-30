package com.programmersbox.fullmultiplatformcompose.steps

import com.intellij.codeInspection.javaDoc.JavadocUIUtil.bindCheckbox
import com.intellij.ide.starters.local.StarterContextProvider
import com.intellij.ide.starters.local.wizard.StarterInitialStep
import com.intellij.ui.dsl.builder.Panel
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderWizardBuilder
import com.programmersbox.fullmultiplatformcompose.utils.NetworkVersions
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

class ComposeStarterStep(
    private val builder: BuilderWizardBuilder,
    private val params: BuilderParams = builder.params,
    contextProvider: StarterContextProvider,
) : StarterInitialStep(contextProvider) {
    override fun addFieldsAfter(layout: Panel) {
        layout.group {
            addCheckboxItem("Include Android", params.hasAndroid) { params.hasAndroid = it }

            if (hostOs == OS.MacOS) {
                addCheckboxItem("Include iOS", params.hasiOS) { params.hasiOS = it }
            }

            addCheckboxItem("Include Desktop", params.hasDesktop) { params.hasDesktop = it }
            addCheckboxItem("Include Web", params.hasWeb) { params.hasWeb = it }

            addDivider()

            addCheckboxItem("Use Material 3", params.compose.useMaterial3) { params.compose.useMaterial3 = it }

            addDivider()

            collapsibleGroup("Libraries") {
                addCheckboxItem(
                    "Use Koin for Dependency Injection",
                    params.library.useKoin
                ) { params.library.useKoin = it }

                addCheckboxItem(
                    "Use Ktor for HTTP client apps",
                    params.library.useKtor
                ) { params.library.useKtor = it }
            }

            addDivider()

            addCheckboxItem(
                "Get latest library versions from remote source?",
                params.remoteVersions
            ) { params.remoteVersions = it }

            row { text("The source is from this plugin's GitHub Repo.") }

            row { browserLink("GitHub Repo", NetworkVersions.githubRepoUrl) }
        }
        /*layout.addCheckboxItem("Include Android", params.hasAndroid) { params.hasAndroid = it }
        if (hostOs == OS.MacOS) {
            layout.addCheckboxItem("Include iOS", params.hasiOS) { params.hasiOS = it }
        }
        layout.addCheckboxItem("Include Desktop", params.hasDesktop) { params.hasDesktop = it }
        layout.addCheckboxItem("Include Web", params.hasWeb) { params.hasWeb = it }
        layout.addDivider()
        layout.addCheckboxItem("Use Material 3", params.compose.useMaterial3) { params.compose.useMaterial3 = it }

        layout.addDivider()

        layout.addCheckboxItem(
            "Use Koin for Dependency Injection",
            params.library.useKoin
        ) { params.library.useKoin = it }
        layout.addCheckboxItem("Use Ktor for HTTP client apps", params.library.useKtor) {
            params.library.useKtor = it
        }

        layout.addDivider()

        layout.addCheckboxItem(
            "Get latest library versions from remote source?",
            params.remoteVersions
        ) { params.remoteVersions = it }
        layout.addSettingsField("", JLabel().apply { text = "The source is from this plugin's GitHub Repo." })

        layout.addExpertField(
            "GitHub:",
            Link(NetworkVersions.githubRepoUrl) { Desktop.getDesktop().browse(URI(NetworkVersions.githubRepoUrl)) }
        )*/
    }

    private fun Panel.addDivider() = separator()

    private fun Panel.addCheckboxItem(
        label: String,
        isChecked: Boolean,
        selectedChangeListener: (Boolean) -> Unit,
    ) {
        row {
            checkBox(label).bindCheckbox(
                get = { isChecked },
                set = selectedChangeListener
            )
        }
    }
}