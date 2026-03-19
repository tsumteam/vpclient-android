package ru.mercury.vpclient.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.mercury.vpclient.core.ui.ktx.installShortcuts
import ru.mercury.vpclient.core.ui.ktx.setLightSystemBars
import ru.mercury.vpclient.core.ui.theme.ClientTheme

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply { setKeepOnScreenCondition { viewModel.stateFlow.value.splashLoading } }
        super.onCreate(savedInstanceState)
        installShortcuts()
        enableEdgeToEdge()
        window.setLightSystemBars()
        setContent { ClientTheme { MainActivityContent() } }
    }
}
