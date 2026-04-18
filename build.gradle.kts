import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.google.hilt) apply false
    alias(libs.plugins.detekt) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    extensions.configure<DetektExtension> {
        config.setFrom(rootProject.file(".github/detekt.yml"))
        buildUponDefaultConfig = true
    }
}
