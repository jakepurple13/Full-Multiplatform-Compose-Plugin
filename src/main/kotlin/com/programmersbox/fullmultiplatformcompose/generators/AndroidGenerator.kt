package com.programmersbox.fullmultiplatformcompose.generators

import com.android.tools.idea.welcome.install.AndroidSdk
import com.intellij.ide.starters.local.StarterModuleBuilder
import com.intellij.openapi.vfs.VirtualFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.dir
import com.programmersbox.fullmultiplatformcompose.utils.file
import com.programmersbox.fullmultiplatformcompose.utils.packages
import java.io.File

class AndroidGenerator(
    params: BuilderParams,
    private val projectName: String,
) : PlatformGenerator(params) {
    override fun setup(root: VirtualFile) {
        AndroidSdk(true)
    }

    override fun File.generateProject(packageSegments: List<String>) {
        val sanitizedPackageName = StarterModuleBuilder.sanitizePackage(projectName)
        dir("android") {
            dir("src") {
                dir("main") {
                    dir("java") {
                        packages(packageSegments) {
                            dir(sanitizedPackageName) {
                                file(
                                    "MainActivity.kt",
                                    "android_mainactivity.kt",
                                    mapOf(
                                        SHARED_NAME to params.sharedName,
                                        PACKAGE_NAME to params.packageName,
                                        "USE_MATERIAL3" to params.compose.useMaterial3,
                                        "LAST_PACKAGE_NAME" to sanitizedPackageName
                                    )
                                )
                            }
                        }
                        dir("res")
                    }
                    file(
                        "AndroidManifest.xml",
                        "android_manifest.xml",
                        mapOf(
                            PACKAGE_NAME to params.packageName,
                            "APP_NAME" to params.android.appName
                        )
                    )
                }
            }
            file(
                "build.gradle.kts",
                "android_build.gradle.kts",
                mapOf(
                    SHARED_NAME to params.sharedName,
                    PACKAGE_NAME to params.packageName,
                    "MINSDK" to params.android.minimumSdk
                )
            )
        }
    }

    override fun File.addToCommon(packageSegments: List<String>) {
        dir("androidMain") {
            packageFilesToPlatformKt(
                packageSegments,
                "default_platform.kt",
                mapOf(
                    SHARED_NAME to params.sharedName,
                    PACKAGE_NAME to params.packageName,
                    "PLATFORM_TYPE" to "Android"
                )
            ) { dir("resources") }

            file(
                "AndroidManifest.xml",
                """
                <?xml version="1.0" encoding="utf-8"?>
                <manifest xmlns:android="http://schemas.android.com/apk/res/android" package="${params.packageName}.${params.sharedName}"/>
                """.trimIndent()
            )
        }
    }

    override fun File.addRunConfig(projectName: String) {
        file("android.run.xml") {
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
    }
}