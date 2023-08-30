package com.programmersbox.fullmultiplatformcompose.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.BuilderTemplateGroup
import com.programmersbox.fullmultiplatformcompose.utils.GeneratorTemplateFile

class IOSGenerator(params: BuilderParams) : PlatformGenerator(params) {
    override fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return listOf(
            GeneratorEmptyDirectory("iosApp/iosApp.xcodeproj/project.xcworkspace/xcshareddata/swiftpm/configuration"),
            GeneratorEmptyDirectory("iosApp/iosApp.xcworkspace/xcshareddata/swiftpm/configuration"),
            GeneratorTemplateFile(
                "iosApp/iosApp.xcodeproj/project.pbxproj",
                ftManager.getCodeTemplate(BuilderTemplateGroup.IOS_PROJECT)
            ),
            GeneratorTemplateFile(
                "iosApp/iosApp.xcodeproj/project.xcworkspace/xcshareddata/IDEWorkspaceChecks.plist",
            ) {
                """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
                    <plist version="1.0">
                    <dict>
                        <key>IDEDidComputeMac32BitWarning</key>
                        <true/>
                    </dict>
                    </plist>
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/iosApp.xcworkspace/xcshareddata/IDEWorkspaceChecks.plist",
            ) {
                """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
                    <plist version="1.0">
                    <dict>
                        <key>IDEDidComputeMac32BitWarning</key>
                        <true/>
                    </dict>
                    </plist>
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/iosApp.xcworkspace/contents.xcworkspacedata"
            ) {
                """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <Workspace
                       version = "1.0">
                       <FileRef
                          location = "group:iosApp.xcodeproj">
                       </FileRef>
                       <FileRef
                          location = "group:Pods/Pods.xcodeproj">
                       </FileRef>
                    </Workspace>
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/iosApp/iOSApp.swift",
                ftManager.getCodeTemplate(BuilderTemplateGroup.IOS_IOSAPP)
            ),
            GeneratorTemplateFile(
                "iosApp/iosApp/ContentView.swift",
            ) {
                """
                //
                //  ContentView.swift
                //  iosApp
                //
                
                import Foundation
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/Podfile"
            ) {
                """
                target 'iosApp' do
                  use_frameworks!
                  platform :ios, '14.1'
                  pod '${params.sharedName}', :path => '../${params.sharedName}'
                end
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/iosApp/Info.plist"
            ) {
                """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
                    <plist version="1.0">
                    <dict>
                    <key>CFBundleDevelopmentRegion</key>
                    <string>${'$'}(DEVELOPMENT_LANGUAGE)</string>
                    <key>CFBundleExecutable</key>
                    <string>${'$'}(EXECUTABLE_NAME)</string>
                    <key>CFBundleIdentifier</key>
                    <string>${'$'}(PRODUCT_BUNDLE_IDENTIFIER)</string>
                    <key>CFBundleInfoDictionaryVersion</key>
                    <string>6.0</string>
                    <key>CFBundleName</key>
                    <string>${'$'}(PRODUCT_NAME)</string>
                    <key>CFBundlePackageType</key>
                    <string>${'$'}(PRODUCT_BUNDLE_PACKAGE_TYPE)</string>
                    <key>CFBundleShortVersionString</key>
                    <string>1.0</string>
                    <key>CFBundleVersion</key>
                    <string>1</string>
                    <key>LSRequiresIPhoneOS</key>
                    <true/>
                    <key>UIApplicationSceneManifest</key>
                    <dict>
                    <key>UIApplicationSupportsMultipleScenes</key>
                    <false/>
                    </dict>
                    <key>UIRequiredDeviceCapabilities</key>
                    <array>
                    <string>armv7</string>
                    </array>
                    <key>UISupportedInterfaceOrientations</key>
                    <array>
                    <string>UIInterfaceOrientationPortrait</string>
                    <string>UIInterfaceOrientationLandscapeLeft</string>
                    <string>UIInterfaceOrientationLandscapeRight</string>
                    </array>
                    <key>UISupportedInterfaceOrientations~ipad</key>
                    <array>
                    <string>UIInterfaceOrientationPortrait</string>
                    <string>UIInterfaceOrientationPortraitUpsideDown</string>
                    <string>UIInterfaceOrientationLandscapeLeft</string>
                    <string>UIInterfaceOrientationLandscapeRight</string>
                    </array>
                    <key>UILaunchScreen</key>
                    <dict/>
                    </dict>
                    </plist>
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/iosApp/Preview Content/Preview Assets.xcassets/Contents.json"
            ) {
                """
                {
                  "info": {
                    "author": "xcode",
                    "version": 1
                  }
                }
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/iosApp/Assets.xcassets/Contents.json"
            ) {
                """
                {
                  "info": {
                    "author": "xcode",
                    "version": 1
                  }
                }
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/Contents.json"
            ) {
                """
                    {
                      "images": [
                        {
                          "idiom": "iphone",
                          "scale": "2x",
                          "size": "20x20"
                        },
                        {
                          "idiom": "iphone",
                          "scale": "3x",
                          "size": "20x20"
                        },
                        {
                          "idiom": "iphone",
                          "scale": "2x",
                          "size": "29x29"
                        },
                        {
                          "idiom": "iphone",
                          "scale": "3x",
                          "size": "29x29"
                        },
                        {
                          "idiom": "iphone",
                          "scale": "2x",
                          "size": "40x40"
                        },
                        {
                          "idiom": "iphone",
                          "scale": "3x",
                          "size": "40x40"
                        },
                        {
                          "idiom": "iphone",
                          "scale": "2x",
                          "size": "60x60"
                        },
                        {
                          "idiom": "iphone",
                          "scale": "3x",
                          "size": "60x60"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "1x",
                          "size": "20x20"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "2x",
                          "size": "20x20"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "1x",
                          "size": "29x29"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "2x",
                          "size": "29x29"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "1x",
                          "size": "40x40"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "2x",
                          "size": "40x40"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "1x",
                          "size": "76x76"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "2x",
                          "size": "76x76"
                        },
                        {
                          "idiom": "ipad",
                          "scale": "2x",
                          "size": "83.5x83.5"
                        },
                        {
                          "idiom": "ios-marketing",
                          "scale": "1x",
                          "size": "1024x1024"
                        }
                      ],
                      "info": {
                        "author": "xcode",
                        "version": 1
                      }
                    }
                """.trimIndent()
            },
            GeneratorTemplateFile(
                "iosApp/iosApp/Assets.xcassets/AccentColor.colorset/Contents.json"
            ) {
                """
                    {
                      "colors": [
                        {
                          "idiom": "universal"
                        }
                      ],
                      "info": {
                        "author": "xcode",
                        "version": 1
                      }
                    }
                """.trimIndent()
            }
        )
    }

    override fun addToCommon(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> = listOf(
        GeneratorTemplateFile(
            "${params.sharedName}/src/iosMain/kotlin/$packageName/${params.sharedName}/platform.kt",
            ftManager.getCodeTemplate(
                if (params.compose.useMaterial3)
                    BuilderTemplateGroup.IOS_PLATFORM3
                else
                    BuilderTemplateGroup.IOS_PLATFORM
            )
        )
    )
}
