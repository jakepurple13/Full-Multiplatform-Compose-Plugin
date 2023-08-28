package com.programmersbox.fullmultiplatformcompose.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderWizardBuilder
import com.programmersbox.fullmultiplatformcompose.utils.CreateComposePanel
import javax.swing.JComponent

class PlatformOptionsStep(
    private val builder: BuilderWizardBuilder,
    private val params: BuilderParams = builder.params
) : ModuleWizardStep() {

    private var sharedName by mutableStateOf(params.sharedName)
    private var packageName by mutableStateOf(params.packageName)

    private var androidName by mutableStateOf(params.android.appName)
    private var androidMinimumSdk by mutableStateOf(params.android.minimumSdk)

    private var iosName by mutableStateOf(params.ios.appName)

    private var hideMe by mutableStateOf(true)

    private val view by lazy {
        CreateComposePanel {
            if (hideMe) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {

                    CreationItem("Common", true) {
                        OutlinedTextField(
                            value = sharedName,
                            onValueChange = { sharedName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Shared Name") }
                        )

                        OutlinedTextField(
                            value = packageName,
                            onValueChange = { packageName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Package Name") }
                        )
                    }

                    CreationItem("Android", params.hasAndroid) {
                        OutlinedTextField(
                            value = androidName,
                            onValueChange = { androidName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("App Name") }
                        )

                        OutlinedTextField(
                            value = androidMinimumSdk.toString(),
                            onValueChange = { androidMinimumSdk = it.toInt().coerceIn(1, 33) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Minimum Sdk") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            )
                        )
                    }

                    CreationItem("iOS", params.hasiOS) {
                        OutlinedTextField(
                            value = iosName,
                            onValueChange = { iosName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("App Name") }
                        )
                    }

                    CreationItem("Web", params.hasWeb) {

                    }

                    CreationItem("Desktop", params.hasDesktop) {

                    }

                }
            }
        }
    }

    override fun onStepLeaving() {
        hideMe = false
        println("LEAVING Options!!!")
    }

    override fun _init() {
        println("SHOWING Options!!!")
        hideMe = true
    }

    override fun getComponent(): JComponent = view

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun CreationItem(item: String, isIncluded: Boolean, content: @Composable ColumnScope.() -> Unit) {
        var showOrHide by remember { mutableStateOf(false) }
        if (isIncluded) {
            Column(
                modifier = Modifier.animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Card(
                    modifier = Modifier.animateContentSize(),
                    onClick = { showOrHide = !showOrHide }
                ) {
                    ListItem(
                        text = { Text(item) },
                        trailing = {
                            IconToggleButton(
                                checked = showOrHide,
                                onCheckedChange = { showOrHide = it }
                            ) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    null,
                                    modifier = Modifier.rotate(animateFloatAsState(if (showOrHide) 180f else 0f).value)
                                )
                            }
                        }
                    )
                }

                AnimatedVisibility(showOrHide) { Column { content() } }
            }
        }
    }

    override fun updateDataModel() {
        params.sharedName = sharedName
        params.packageName = packageName
        params.android.appName = androidName
        params.android.minimumSdk = androidMinimumSdk
        params.ios.appName = iosName
    }
}