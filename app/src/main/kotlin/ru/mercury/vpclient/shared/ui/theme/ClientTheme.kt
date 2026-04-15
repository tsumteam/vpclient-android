package ru.mercury.vpclient.shared.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import ru.mercury.vpclient.shared.data.MAX_FONT_SCALE
import kotlin.math.min

@Composable
fun ClientTheme(
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = density.density,
            fontScale = min(density.fontScale, MAX_FONT_SCALE)
        )
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            content = content
        )
    }
}
