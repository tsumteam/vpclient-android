package ru.mercury.vpclient.features.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.mercury.vpclient.core.ui.ktx.reportShortcutUsed
import ru.mercury.vpclient.core.ui.ktx.setLightSystemBars
import ru.mercury.vpclient.core.ui.theme.VPClientTheme

@AndroidEntryPoint
class DebugActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setLightSystemBars()
        reportShortcutUsed()
        setContent { VPClientTheme { DebugScreen() } }
    }
}
