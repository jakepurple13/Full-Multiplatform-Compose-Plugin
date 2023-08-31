package com.programmersbox.fullmultiplatformcompose.generators

import com.android.tools.idea.welcome.install.AndroidSdk
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.intellij.ide.starters.local.StarterModuleBuilder
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderTemplateGroup
import com.programmersbox.fullmultiplatformcompose.utils.GeneratorTemplateFile

class AndroidGenerator(params: BuilderParams, private val projectName: String) : PlatformGenerator(params) {

    override fun setup() {
        AndroidSdk(true)
    }

    override fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        val sanitizedPackageName = StarterModuleBuilder.sanitizePackage(projectName)
        return listOf(
            GeneratorTemplateFile(
                "android/src/main/java/$packageName/$sanitizedPackageName/MainActivity.kt",
                ftManager.getCodeTemplate(BuilderTemplateGroup.ANDROID_MAINACTIVITY)
            ),
            GeneratorTemplateFile(
                "android/src/main/AndroidManifest.xml",
                ftManager.getCodeTemplate(BuilderTemplateGroup.ANDROID_MANIFEST)
            ),
            GeneratorTemplateFile(
                "android/build.gradle.kts",
                ftManager.getCodeTemplate(BuilderTemplateGroup.ANDROID_BUILD)
            ),
        )
    }

    override fun addToCommon(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return listOf(
            GeneratorTemplateFile(
                "${params.sharedName}/src/androidMain/kotlin/$packageName/${params.sharedName}/platform.kt",
                ftManager.getCodeTemplate(BuilderTemplateGroup.DEFAULT_PLATFORM)
            ),
            GeneratorTemplateFile(
                "${params.sharedName}/src/androidMain/AndroidManifest.xml",
                ftManager.getCodeTemplate(BuilderTemplateGroup.COMMON_ANDROID_MANIFEST)
            )
        )
    }

    override fun addRunConfig(ftManager: FileTemplateManager, projectName: String): List<GeneratorAsset> = listOf(
        GeneratorTemplateFile(
            ".run/android.run.xml"
        ) {
            """
            <component name="ProjectRunConfigurationManager">
              <configuration default="false" name="android" type="AndroidRunConfigurationType" factoryName="Android App">
                <module name="$projectName.android.main" />
                <option name="DEPLOY" value="true" />
                <option name="DEPLOY_APK_FROM_BUNDLE" value="false" />
                <option name="DEPLOY_AS_INSTANT" value="false" />
                <option name="ARTIFACT_NAME" value="" />
                <option name="PM_INSTALL_OPTIONS" value="" />
                <option name="ALL_USERS" value="false" />
                <option name="ALWAYS_INSTALL_WITH_PM" value="false" />
                <option name="CLEAR_APP_STORAGE" value="false" />
                <option name="DYNAMIC_FEATURES_DISABLED_LIST" value="" />
                <option name="ACTIVITY_EXTRA_FLAGS" value="" />
                <option name="MODE" value="default_activity" />
                <option name="CLEAR_LOGCAT" value="false" />
                <option name="SHOW_LOGCAT_AUTOMATICALLY" value="false" />
                <option name="INSPECTION_WITHOUT_ACTIVITY_RESTART" value="false" />
                <option name="TARGET_SELECTION_MODE" value="DEVICE_AND_SNAPSHOT_COMBO_BOX" />
                <option name="DEBUGGER_TYPE" value="Java" />
                <Java />
                <Profilers>
                  <option name="ADVANCED_PROFILING_ENABLED" value="false" />
                  <option name="STARTUP_PROFILING_ENABLED" value="false" />
                  <option name="STARTUP_CPU_PROFILING_ENABLED" value="false" />
                  <option name="STARTUP_CPU_PROFILING_CONFIGURATION_NAME" value="Java/Kotlin Method Sample (legacy)" />
                  <option name="STARTUP_NATIVE_MEMORY_PROFILING_ENABLED" value="false" />
                  <option name="NATIVE_MEMORY_SAMPLE_RATE_BYTES" value="2048" />
                </Profilers>
                <option name="DEEP_LINK" value="" />
                <option name="ACTIVITY_CLASS" value="" />
                <option name="SEARCH_ACTIVITY_IN_GLOBAL_SCOPE" value="false" />
                <option name="SKIP_ACTIVITY_VALIDATION" value="false" />
                <method v="2">
                  <option name="Android.Gradle.BeforeRunTask" enabled="true" />
                </method>
              </configuration>
            </component>
            """.trimIndent()
        }
    )
}
