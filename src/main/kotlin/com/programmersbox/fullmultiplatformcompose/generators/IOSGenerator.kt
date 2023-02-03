package com.programmersbox.fullmultiplatformcompose.generators

import com.programmersbox.fullmultiplatformcompose.BuilderParams
import com.programmersbox.fullmultiplatformcompose.utils.dir
import com.programmersbox.fullmultiplatformcompose.utils.file
import java.io.File

class IOSGenerator(params: BuilderParams) : PlatformGenerator(params) {
    override fun File.generateProject(packageSegments: List<String>) {
        dir("iosApp") {
            dir("iosApp") {

                file(
                    "iOSApp.swift",
                    "ios_iosapp.swift",
                    mapOf(SHARED_NAME to params.sharedName)
                )

                file("ContentView.swift") {
                    """
                    //
                    //  ContentView.swift
                    //  iosApp
                    //
                    
                    import Foundation
                    """.trimIndent()
                }

                file("Info.plist") {
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
                }

                dir("Assets.xcassets") {

                    dir("AccentColor.colorset") {
                        file("Contents.json") {
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
                    }

                    dir("AppIcon.appiconset") {
                        file("Contents.json") {
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
                        }
                    }

                    file("Contents.json") {
                        """
                        {
                          "info": {
                            "author": "xcode",
                            "version": 1
                          }
                        }
                        """.trimIndent()
                    }
                }

                dir("Preview Content") {
                    dir("Preview Assets.xcassets") {
                        file("Contents.json") {
                            """
                            {
                              "info": {
                                "author": "xcode",
                                "version": 1
                              }
                            }
                            """.trimIndent()
                        }
                    }
                }

            }

            dir("iosApp.xcodeproj") {

                dir("project.xcworkspace") {
                    dir("xcshareddata") {
                        dir("swiftpm") {
                            dir("configuration")
                        }

                        file("IDEWorkspaceChecks.plist") {
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
                        }
                    }
                }

                file(
                    "project.pbxproj",
                    "ios_project.pbxproj",
                    mapOf(SHARED_NAME to params.sharedName)
                )

            }

            dir("iosApp.xcworkspace") {
                dir("xcshareddata") {
                    dir("swiftpm") {
                        dir("configuration")
                    }

                    file("IDEWorkspaceChecks.plist") {
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
                    }
                }

                file("contents.xcworkspacedata") {
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
                }
            }

            file("Podfile") {
                """
                target 'iosApp' do
                  use_frameworks!
                  platform :ios, '14.1'
                  pod '${params.sharedName}', :path => '../${params.sharedName}'
                end
                """.trimIndent()
            }

        }
    }

    override fun File.addToCommon(packageSegments: List<String>) {
        dir("iosMain") {
            packageFilesToPlatformKt(
                packageSegments,
                if (params.compose.useMaterial3) "ios_platform3.kt" else "ios_platform.kt",
                mapOf(
                    SHARED_NAME to params.sharedName,
                    PACKAGE_NAME to params.packageName,
                    "APP_NAME" to params.ios.appName
                )
            )
        }
    }
}