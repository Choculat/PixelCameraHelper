# Pixel Camera Helper

Lets Pixel Camera's photo thumbnail open a gallery app other than Google Photos
(e.g. on GrapheneOS, where Google Photos isn't installed).

## Why it's needed

When you tap the thumbnail in Pixel Camera, GCam does a hard package check —
`getPackageManager().getPackageInfo("com.google.android.apps.photos", ...)`.
If that package is missing it logs `Cannot find Photos package info. Canceling.`
and shows a **"Photos required — install Google Photos from Play Store"** dialog.
It never fires a REVIEW intent, so a normal app that merely *handles* REVIEW is
never reached.

## How it works

This app installs **under the package name `com.google.android.apps.photos`**
(see `applicationId` in [app/build.gradle.kts](app/build.gradle.kts)), so GCam's
presence check passes. Its `ReviewActivity` catches the REVIEW intent GCam then
fires and re-launches it at whatever gallery app you picked in the UI.

## If you already have Google Photos installed

You can't. This app claims the same package name (`com.google.android.apps.photos`),
so Android treats it as the same app that's already on your phone. Because this
build is self-signed and the real Google Photos is signed by Google, the install
is rejected with a signature mismatch — you'll see **"App not installed"**
(`INSTALL_FAILED_UPDATE_INCOMPATIBLE` over adb). Uninstall real Google Photos
first if you actually want to use this instead.

## Build / install

```
JAVA_HOME=<a JDK 17+> ./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Open "Pixel Camera Helper", tap the gallery app you want. Done.
