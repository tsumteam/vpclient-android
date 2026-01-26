package ru.mercury.vpclient.core.ui.ktx

import android.provider.Settings
import android.view.Window
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.view.WindowCompat

fun Window.setLightSystemBars() {
    WindowCompat.getInsetsController(this, decorView).apply {
        isAppearanceLightStatusBars = true
        isAppearanceLightNavigationBars = true
    }
}

@Composable
fun isThreeButtonNav(): Boolean {
    val context = LocalContext.current
    val v = Settings.Secure.getInt(context.contentResolver, "navigation_mode", -1)
    if (v in 0..2) return v == 0
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val gestures = WindowInsets.systemGestures
    val hasGestureInsets = gestures.getLeft(density, layoutDirection) > 0 || gestures.getRight(density, layoutDirection) > 0 || gestures.getTop(density) > 0 || gestures.getBottom(density) > 0
    return !hasGestureInsets
}
