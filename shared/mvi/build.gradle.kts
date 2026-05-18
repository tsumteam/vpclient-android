plugins {
    alias(libs.plugins.android.library)
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

android {
    namespace = "ru.mercury.vpclient.shared.mvi"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }
}

dependencies {
    implementation(projects.shared.coroutines)
}
