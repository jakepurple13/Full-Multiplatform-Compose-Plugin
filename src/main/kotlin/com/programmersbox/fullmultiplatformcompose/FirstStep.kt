package com.programmersbox.fullmultiplatformcompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.programmersbox.fullmultiplatformcompose.utils.CreateComposePanel
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import javax.swing.JComponent

class FirstStep(
    private val builder: BuilderWizardBuilder,
    private val params: BuilderParams = builder.params
) : ModuleWizardStep() {
    private var hasAndroid by mutableStateOf(params.hasAndroid)
    private var hasiOS by mutableStateOf(params.hasiOS)
    private var hasDesktop by mutableStateOf(params.hasDesktop)
    private var hasWeb by mutableStateOf(params.hasWeb)

    override fun getComponent(): JComponent = CreateComposePanel {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CheckRow("Include Android", hasAndroid) { hasAndroid = it }
            CheckRow("Include Desktop", hasDesktop) { hasDesktop = it }
            if (hostOs == OS.MacOS) {
                CheckRow("Include iOS", hasiOS) { hasiOS = it }
            }
            CheckRow("Include Web", hasWeb) { hasWeb = it }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun CheckRow(text: String, checked: Boolean, checkChange: (Boolean) -> Unit) {
        Card(onClick = { checkChange(!checked) }) {
            ListItem(
                icon = {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = checkChange
                    )
                },
                text = { Text(text) },
            )
        }
    }

    override fun updateDataModel() {
        params.hasAndroid = hasAndroid
        params.hasWeb = hasWeb
        params.hasDesktop = hasDesktop
        params.hasiOS = hasiOS
    }

}