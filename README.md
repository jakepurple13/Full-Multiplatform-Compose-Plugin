# Full-Multiplatform-Compose-Plugin

An intellij plugin to create multiplatform compose projects, covering everything! JS, Android, IOS, and desktop!

<p align="center">
  <img src="/screenshots/plugin_start.png" width="32%"/>
  <img src="/screenshots/plugin_options.png" width="32%"/>
</p>

Note: IOS will ONLY work on macOS computers. I am sorry.

# Running

I tried to make this plugin as plug-n-play as possible!
Android's run configuration should be in place already.

Desktop, web, and iOS are a bit special.

### For Desktop:

Either run `MainKt` or find the `main.kt` file and press run!

### For Web:

Either run `Run Web` or run `./gradlew :jsApp:jsBrowserDevelopmentRun`

### For iOS:

Open the `iosApp` in XCode via `iosApp.xcworkspace` and run as normal from there.