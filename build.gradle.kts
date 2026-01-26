plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.google.hilt) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
    alias(libs.plugins.detekt)
}

detekt {
    config.setFrom("$projectDir/config/detekt/detekt.yml")
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
}
