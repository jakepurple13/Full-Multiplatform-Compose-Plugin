package com.programmersbox.fullmultiplatformcompose.generators

import com.programmersbox.fullmultiplatformcompose.BuilderParams
import java.io.File

abstract class PlatformGenerator(protected val params: BuilderParams) {

    fun generate(
        file: File,
        packageSegments: List<String>
    ) = file.generateProject(packageSegments)

    protected abstract fun File.generateProject(packageSegments: List<String>)

}