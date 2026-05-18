plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

android {
    namespace = "ru.mercury.vpclient.shared.coroutines"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }
}

dependencies {
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.lifecycle.viewmodel)
    api(libs.timber)
    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compiler)
}
