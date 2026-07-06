plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "sh.margot.pixelcamerahelper"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        // Masquerade as Google Photos so Pixel Camera's getPackageInfo() check passes.
        applicationId = "com.google.android.apps.photos"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
