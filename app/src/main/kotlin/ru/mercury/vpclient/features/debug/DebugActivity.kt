package ru.mercury.vpclient.features.debug

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.mercury.vpclient.shared.ui.ktx.reportShortcutUsed
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

@AndroidEntryPoint
class DebugActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        reportShortcutUsed()
        setContent { ClientTheme { DebugScreen() } }
    }
}
