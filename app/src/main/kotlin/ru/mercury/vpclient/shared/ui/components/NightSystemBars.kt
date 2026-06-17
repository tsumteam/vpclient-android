package ru.mercury.vpclient.shared.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun NightSystemBars() {
    val activity = LocalActivity.current
    val view = LocalView.current

    DisposableEffect(activity, view) {
        val controller = activity?.window?.let { window ->
            WindowCompat.getInsetsController(window, view)
        }

        when (controller) {
            null -> onDispose {}
            else -> {
                val previousLightStatusBars = controller.isAppearanceLightStatusBars
                val previousLightNavigationBars = controller.isAppearanceLightNavigationBars

                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false

                onDispose {
                    controller.isAppearanceLightStatusBars = previousLightStatusBars
                    controller.isAppearanceLightNavigationBars = previousLightNavigationBars
                }
            }
        }
    }
}
