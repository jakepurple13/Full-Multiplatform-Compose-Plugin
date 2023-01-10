package com.programmersbox.fullmultiplatformcompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BuilderParams {
    var hasAndroid: Boolean by mutableStateOf(false)
    var hasWeb: Boolean by mutableStateOf(false)
    var hasiOS: Boolean by mutableStateOf(false)
    var hasDesktop: Boolean by mutableStateOf(false)

    var packageName by mutableStateOf("com.example")
    var sharedName by mutableStateOf("common")

    val android = Android()
    val ios = IOS()

    val compose = Compose()
}

class Android {
    var minimumSdk: Int by mutableStateOf(24)
    var appName: String by mutableStateOf("My Application")
}

class IOS {
    var appName: String by mutableStateOf("My Application")
}

class Compose {
    var useMaterial3: Boolean by mutableStateOf(true)
}