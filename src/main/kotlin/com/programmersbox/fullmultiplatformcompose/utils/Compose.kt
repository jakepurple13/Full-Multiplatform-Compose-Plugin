package com.programmersbox.fullmultiplatformcompose.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.JComponent
import javax.swing.UIManager
import java.awt.Color as AWTColor

fun CreateComposePanel(
    modification: ComposePanel.() -> Unit = {},
    content: @Composable () -> Unit
): JComponent = ComposePanel()
    .apply(modification)
    .apply {
        setContent {
            Theme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    content = content
                )
            }
        }
    }

enum class FileDialogMode(internal val id: Int) { Load(FileDialog.LOAD), Save(FileDialog.SAVE) }

@Composable
fun FileDialog(
    mode: FileDialogMode,
    title: String = "Choose a file",
    parent: Frame? = null,
    block: FileDialog.() -> Unit = {},
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, title, mode.id) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(directory + File.separator + file)
                }
            }
        }.apply(block)
    },
    dispose = FileDialog::dispose
)

interface SwingColor {
    val background: Color
    val onBackground: Color
}

@Composable
fun SwingColor(): SwingColor {
    val swingColor = remember { SwingColorImpl() }

    val messageBus = remember {
        ApplicationManager.getApplication().messageBus.connect()
    }

    remember(messageBus) {
        messageBus.subscribe(
            LafManagerListener.TOPIC,
            ThemeChangeListener(swingColor::updateCurrentColors)
        )
    }

    DisposableEffect(messageBus) {
        onDispose {
            messageBus.disconnect()
        }
    }

    return swingColor
}

private class SwingColorImpl : SwingColor {
    private val _backgroundState: MutableState<Color> = mutableStateOf(getBackgroundColor)
    private val _onBackgroundState: MutableState<Color> = mutableStateOf(getOnBackgroundColor)

    override val background: Color get() = _backgroundState.value
    override val onBackground: Color get() = _onBackgroundState.value

    private val getBackgroundColor get() = getColor(BACKGROUND_KEY)
    private val getOnBackgroundColor get() = getColor(ON_BACKGROUND_KEY)

    fun updateCurrentColors() {
        _backgroundState.value = getBackgroundColor
        _onBackgroundState.value = getOnBackgroundColor
    }

    private val AWTColor.asComposeColor: Color get() = Color(red, green, blue, alpha)
    private fun getColor(key: String): Color = UIManager.getColor(key).asComposeColor

    companion object {
        private const val BACKGROUND_KEY = "Panel.background"
        private const val ON_BACKGROUND_KEY = "Panel.foreground"
    }
}

internal class ThemeChangeListener(
    val updateColors: () -> Unit
) : LafManagerListener {
    override fun lookAndFeelChanged(source: LafManager) {
        updateColors()
    }
}
