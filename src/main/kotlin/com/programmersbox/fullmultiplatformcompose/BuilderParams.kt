package com.programmersbox.fullmultiplatformcompose

class BuilderParams {
    var hasAndroid: Boolean = false
    var hasWeb: Boolean = false
    var hasiOS: Boolean = false
    var hasDesktop: Boolean = false

    var packageName = "com.example"
    var sharedName = "common"

    val android = Android()
}

data class Android(
    var minimumSdk: Int = 24,
    var appName: String = "My Application"
)