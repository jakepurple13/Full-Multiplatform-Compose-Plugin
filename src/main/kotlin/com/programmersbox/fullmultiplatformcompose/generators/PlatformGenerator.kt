package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.openapi.vfs.VirtualFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.dir
import com.programmersbox.fullmultiplatformcompose.utils.file
import com.programmersbox.fullmultiplatformcompose.utils.packages
import java.io.File

abstract class PlatformGenerator(protected val params: BuilderParams) {

    open fun setup(root: VirtualFile) {}

    fun generate(
        file: File,
        packageSegments: List<String>
    ) = file.generateProject(packageSegments)

    protected abstract fun File.generateProject(packageSegments: List<String>)

    fun commonFiles(
        file: File,
        packageSegments: List<String>
    ) = file.addToCommon(packageSegments)

    protected open fun File.addToCommon(packageSegments: List<String>) {}

    protected fun File.packageFilesToPlatformKt(
        packageSegments: List<String>,
        templateName: String,
        attributes: Map<String, Any>,
        additions: File.() -> Unit = {}
    ) {
        dir("kotlin") {
            packages(packageSegments) {
                dir(params.sharedName) {
                    file(
                        "platform.kt",
                        templateName,
                        attributes
                    )
                }
            }
            additions()
        }
    }

    fun addRunConfiguration(file: File, projectName: String) = file.addRunConfig(projectName)

    protected open fun File.addRunConfig(projectName: String) {}

}