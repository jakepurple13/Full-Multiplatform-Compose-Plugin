package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.openapi.vfs.VirtualFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.*
import kotlinx.coroutines.runBlocking

internal const val SHARED_NAME = "SHARED_NAME"
internal const val PACKAGE_NAME = "PACKAGE_NAME"

class CommonGenerator(
    private val params: BuilderParams,
    private val projectName: String
) {

    private val network = NetworkVersions()

    private val androidGenerator = AndroidGenerator(params)
    private val webGenerator = WebGenerator(params)
    private val desktopGenerator = DesktopGenerator(params)
    private val iosGenerator = IOSGenerator(params)

    private fun BuilderParams.hasAndroid() = "HAS_ANDROID" to hasAndroid
    private fun BuilderParams.hasIOS() = "HAS_IOS" to hasiOS
    private fun BuilderParams.hasWeb() = "HAS_WEB" to hasWeb
    private fun BuilderParams.hasDesktop() = "HAS_DESKTOP" to hasDesktop

    fun generate(
        root: VirtualFile,
    ) {
        try {
            val packageSegments = params.packageName.split(".")
            /*groupId
                .split(".")
                .toMutableList()
                .apply { add(artifactId) }
                .toList()*/

            val generatorList = listOfNotNull(
                if (params.hasAndroid) androidGenerator else null,
                if (params.hasDesktop) desktopGenerator else null,
                if (params.hasWeb) webGenerator else null,
                if (params.hasiOS) iosGenerator else null
            )

            root.build {
                file(
                    "build.gradle.kts",
                    "project_build.gradle.kts",
                    mapOf(
                        PACKAGE_NAME to params.packageName,
                        "APP_NAME" to params.android.appName,
                        params.hasAndroid(),
                        params.hasDesktop(),
                        params.hasIOS(),
                        params.hasWeb(),
                    )
                )

                file(
                    "settings.gradle.kts",
                    "project_settings.gradle.kts",
                    mapOf(
                        PACKAGE_NAME to params.packageName,
                        SHARED_NAME to params.sharedName,
                        "APP_NAME" to projectName,
                        params.hasAndroid(),
                        params.hasDesktop(),
                        params.hasIOS(),
                        params.hasWeb(),
                    )
                )

                val versions = runBlocking { network.getVersions(params.remoteVersions) }

                file(
                    "gradle.properties",
                    "project_gradle.properties",
                    mapOf(
                        "COMPOSE" to versions.composeVersion,
                        "KOTLIN" to versions.kotlinVersion,
                        "AGP" to versions.agpVersion
                    )
                )

                dir(params.sharedName) {
                    dir("src") {
                        dir("commonMain") {
                            dir("kotlin") {
                                packages(packageSegments) {
                                    dir(params.sharedName) {
                                        file(
                                            "platform.kt",
                                            "common_platform.kt",
                                            mapOf(
                                                SHARED_NAME to params.sharedName,
                                                PACKAGE_NAME to params.packageName,
                                            )
                                        )

                                        file(
                                            "App.kt",
                                            "common_app.kt",
                                            mapOf(
                                                SHARED_NAME to params.sharedName,
                                                PACKAGE_NAME to params.packageName,
                                                "USE_MATERIAL3" to params.compose.useMaterial3,
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        generatorList.forEach { it.commonFiles(this, packageSegments) }
                    }

                    file(
                        "build.gradle.kts",
                        "common_build.gradle.kts",
                        mapOf(
                            SHARED_NAME to params.sharedName,
                            PACKAGE_NAME to params.packageName,
                            "MINSDK" to params.android.minimumSdk,
                            params.hasAndroid(),
                            params.hasDesktop(),
                            params.hasIOS(),
                            params.hasWeb(),
                            "USE_MATERIAL3" to params.compose.useMaterial3,
                        )
                    )
                }

                generatorList.forEach { it.generate(this, packageSegments) }

                dir(".idea") {
                    arrayOf("gradle.xml").forEach { fileName ->
                        file(
                            fileName,
                            "idea_${fileName}",
                            mapOf(
                                SHARED_NAME to params.sharedName,
                                PACKAGE_NAME to params.packageName,
                                params.hasAndroid(),
                                params.hasDesktop(),
                                params.hasIOS(),
                                params.hasWeb(),
                            )
                        )
                    }
                }

                dir(".run") {
                    if (params.hasWeb) {
                        file("Run_Web.run.xml") {
                            """
                                <component name="ProjectRunConfigurationManager">
                                  <configuration default="false" name="Run Web" type="GradleRunConfiguration" factoryName="Gradle">
                                    <ExternalSystemSettings>
                                      <option name="executionName" />
                                      <option name="externalProjectPath" value="${'$'}PROJECT_DIR${'$'}" />
                                      <option name="externalSystemIdString" value="GRADLE" />
                                      <option name="scriptParameters" value="" />
                                      <option name="taskDescriptions">
                                        <list />
                                      </option>
                                      <option name="taskNames">
                                        <list>
                                          <option value=":jsApp:jsBrowserDevelopmentRun" />
                                        </list>
                                      </option>
                                      <option name="vmOptions" />
                                    </ExternalSystemSettings>
                                    <ExternalSystemDebugServerProcess>true</ExternalSystemDebugServerProcess>
                                    <ExternalSystemReattachDebugProcess>true</ExternalSystemReattachDebugProcess>
                                    <DebugAllEnabled>false</DebugAllEnabled>
                                    <method v="2" />
                                  </configuration>
                                </component>
                            """.trimIndent()
                        }
                    }

                    if (params.hasDesktop) {
                        file("Run_Desktop.run.xml") {
                            """
                                <component name="ProjectRunConfigurationManager">
                                  <configuration default="false" name="MainKt" type="JetRunConfigurationType" nameIsGenerated="true">
                                    <option name="MAIN_CLASS_NAME" value="MainKt" />
                                    <module name="$projectName.desktop.jvmMain" />
                                    <shortenClasspath name="NONE" />
                                    <method v="2">
                                      <option name="Make" enabled="true" />
                                    </method>
                                  </configuration>
                                </component>
                            """.trimIndent()
                        }
                    }

                    if (params.hasAndroid) {
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
            }
            root.refresh(false, true)
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
    }
}