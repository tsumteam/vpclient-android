import com.android.build.api.variant.impl.VariantOutputImpl
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
}

private val gitCommitsCount: Int by lazy {
    try {
        val isWindows = System.getProperty("os.name").contains("Windows", ignoreCase = true)
        val processBuilder = when {
            isWindows -> ProcessBuilder("cmd", "/c", "git", "rev-list", "--count", "HEAD")
            else -> ProcessBuilder("git", "rev-list", "--count", "HEAD") // Unix
        }
        processBuilder.redirectErrorStream(true)
        processBuilder.start().inputStream.bufferedReader(StandardCharsets.UTF_8).readLine().trim().toInt()
    } catch (_: Exception) {
        1
    }
}
private val vpclientApiKey: String by lazy {
    val key = gradleLocalProperties(rootDir, providers).getProperty("VP_VPCLIENT_API_KEY").orEmpty().ifEmpty { System.getenv("VP_VPCLIENT_API_KEY") }
    if (key.isEmpty()) throw GradleException("VP_VPCLIENT_API_KEY is empty")
    key
}
private val appmetricaApiKey: String by lazy {
    val key = gradleLocalProperties(rootDir, providers).getProperty("APPMETRICA_API_KEY").orEmpty().ifEmpty { System.getenv("APPMETRICA_API_KEY") }
    if (key.isEmpty()) throw GradleException("APPMETRICA_API_KEY is empty")
    key
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

android {
    namespace = "ru.mercury.vpclient"
    compileSdk = libs.versions.compile.sdk.get().toInt()
    flavorDimensions += "env"

    defaultConfig {
        applicationId = "ru.mercury.vpclient"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionName = "1.0.0"
        versionCode = gitCommitsCount
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "VP_VPCLIENT_API_KEY", "\"$vpclientApiKey\"")
        buildConfigField("String", "APPMETRICA_API_KEY", "\"$appmetricaApiKey\"")
    }


    productFlavors {
        create("prod") {
            dimension = "env"
            buildConfigField("String", "VPCLIENT_ENV", "\"prod\"")
        }
        create("uat") {
            dimension = "env"
            applicationIdSuffix = ".uat"
            buildConfigField("String", "VPCLIENT_ENV", "\"uat\"")
        }
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            buildConfigField("String", "VPCLIENT_ENV", "\"dev\"")
        }
    }

    signingConfigs {
        val keystoreDebugPropertiesFile = rootProject.file("config/keystore-debug.properties")
        if (keystoreDebugPropertiesFile.exists()) {
            val keystoreDebugProperties = Properties()
            keystoreDebugProperties.load(FileInputStream(keystoreDebugPropertiesFile))
            getByName("debug") {
                keyAlias = keystoreDebugProperties["keyAlias"] as String
                keyPassword = keystoreDebugProperties["keyPassword"] as String
                storeFile = file(keystoreDebugProperties["storeFile"] as String)
                storePassword = keystoreDebugProperties["storePassword"] as String
            }
        }

        val keystoreReleaseProperties = Properties()
        val keystoreReleasePropertiesFile = rootProject.file("config/keystore-release.properties")
        if (keystoreReleasePropertiesFile.exists()) {
            keystoreReleaseProperties.load(FileInputStream(keystoreReleasePropertiesFile))
            create("release") {
                keyAlias = keystoreReleaseProperties["keyAlias"] as String
                keyPassword = keystoreReleaseProperties["keyPassword"] as String
                storeFile = file(keystoreReleaseProperties["storeFile"] as String)
                storePassword = keystoreReleaseProperties["storePassword"] as String
            }
        } else {
            val keystoreAlias = System.getenv("KEYSTORE_KEY_ALIAS").orEmpty()
            val keystorePassword = System.getenv("KEYSTORE_KEY_PASSWORD").orEmpty()
            val keystoreStorePassword = System.getenv("KEYSTORE_STORE_PASSWORD").orEmpty()
            val keystoreFile = System.getenv("KEYSTORE_FILE").orEmpty()
            if (keystoreAlias.isNotEmpty()) {
                keystoreReleaseProperties["keyAlias"] = keystoreAlias
                keystoreReleaseProperties["keyPassword"] = keystorePassword
                keystoreReleaseProperties["storePassword"] = keystoreStorePassword
                keystoreReleaseProperties["storeFile"] = keystoreFile
                create("release") {
                    keyAlias = keystoreReleaseProperties["keyAlias"] as String
                    keyPassword = keystoreReleaseProperties["keyPassword"] as String
                    storeFile = file(keystoreReleaseProperties["storeFile"] as String)
                    storePassword = keystoreReleaseProperties["storePassword"] as String
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = if (signingConfigs.findByName("release") != null) signingConfigs.getByName("release") else null
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            signingConfig = if (signingConfigs.findByName("debug") != null) signingConfigs.getByName("debug") else null
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a") // Samsung A35 / Pixel 7 / Emulator
            isUniversalApk = false
        }
    }

    packaging {
        resources {
            excludes += "META-INF/*.version"
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/native-image/**"
            excludes += "META-INF/*.kotlin_module"
            pickFirsts += "META-INF/services/**"
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            if (output is VariantOutputImpl) {
                output.outputFileName = "VPClient-${variant.name}-v${android.defaultConfig.versionName}(${android.defaultConfig.versionCode}).apk"
            }
        }
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compiler)
    implementation(libs.google.material)
    implementation(libs.google.mlkit.barcode.scanning)
    implementation(libs.google.zxing)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.androidx.room.compiler)
    debugImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.junit)

    implementation(libs.appmetrica.analytics)
    implementation(libs.coil.compose)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.timber)

    debugImplementation(libs.okhttp.logging.interceptor)
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.ktor.client.mock)

    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.test.ext.junit.ktx)
    androidTestImplementation(libs.androidx.work.testing)
}

tasks.register("printVersion") {
    doLast {
        println("VERSION_NAME=${android.defaultConfig.versionName}")
        println("VERSION_CODE=${android.defaultConfig.versionCode}")
    }
}
