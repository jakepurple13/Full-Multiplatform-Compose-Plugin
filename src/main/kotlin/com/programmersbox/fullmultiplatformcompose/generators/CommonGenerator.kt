package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.mock.MockVirtualFile.dir
import com.intellij.openapi.vfs.VirtualFile
import com.programmersbox.fullmultiplatformcompose.utils.build
import com.programmersbox.fullmultiplatformcompose.utils.dir

class CommonGenerator(
    hasAndroid: Boolean,
    hasWeb: Boolean,
    hasiOS: Boolean,
    hasDesktop: Boolean
) {

    fun generate(
        root: VirtualFile,
        artifactId: String,
        groupId: String,
        modules: List<String>,
        initializers: List<String>,
    ) {
        try {
            val packageSegments = groupId
                .split(".")
                .toMutableList()
                .apply { add(artifactId) }
                .toList()
            root.build {
                dir("src") {

                }
            }
            root.refresh(false, true)
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
    }

}