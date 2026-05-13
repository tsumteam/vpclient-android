package ru.mercury.vpclient.shared.ui.preview.wrapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

class ThemeWrapper: PreviewWrapperProvider {

    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        ClientTheme {
            content()
        }
    }
}
