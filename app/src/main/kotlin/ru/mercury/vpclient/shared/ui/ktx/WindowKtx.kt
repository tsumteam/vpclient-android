package ru.mercury.vpclient.shared.ui.ktx

import android.view.Window
import androidx.core.view.WindowCompat

fun Window.setLightSystemBars() {
    WindowCompat.getInsetsController(this, decorView).apply {
        isAppearanceLightStatusBars = true
        isAppearanceLightNavigationBars = true
    }
}
