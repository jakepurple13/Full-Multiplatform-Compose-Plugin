package com.programmersbox.fullmultiplatformcompose.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel

fun CreateComposePanel(content: @Composable () -> Unit) = ComposePanel().apply {
    setContent {
        MaterialTheme(
            colors = if (isSystemInDarkTheme()) darkColors() else lightColors(),
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = content
            )
        }
    }
}