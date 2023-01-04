package com.programmersbox.fullmultiplatformcompose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.awt.SwingPanel
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.openapi.Disposable
import javax.swing.JComponent
import javax.swing.JPanel

class FirstStep(private val builder: BuilderWizardBuilder) : ModuleWizardStep() {
    private var hasAndroid by mutableStateOf(builder.hasAndroid)
    private var hasiOS by mutableStateOf(builder.hasiOS)
    private var hasDesktop by mutableStateOf(builder.hasDesktop)
    private var hasWeb by mutableStateOf(builder.hasWeb)

    override fun getComponent(): JComponent = ComposePanel().apply {
        setContent {
            MaterialTheme(
                if (isSystemInDarkTheme()) darkColors() else lightColors()
            ) {
                Column {
                    CheckRow("Include Android", hasAndroid) { hasAndroid = it }
                    CheckRow("Include Desktop", hasDesktop) { hasDesktop = it }
                    CheckRow("Include iOS", hasiOS) { hasiOS = it }
                    CheckRow("Include Web", hasWeb) { hasWeb = it }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun CheckRow(text: String, checked: Boolean, checkChange: (Boolean) -> Unit) {
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

    override fun updateDataModel() {
        builder.hasAndroid = hasAndroid
        builder.hasWeb = hasWeb
        builder.hasDesktop = hasDesktop
        builder.hasiOS = hasiOS
    }

}