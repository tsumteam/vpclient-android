package ru.mercury.vpclient.shared.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1F1F1F),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF666061),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1F1F1F),
    surface = Color(0xFFF6F3F3),
    onSurface = Color(0xFF1F1F1F),
    surfaceVariant = Color(0xFFEDE7E7),
    surfaceContainer = Color(0xFFF7F7F7),
    surfaceContainerHigh = Color(0xFFF5F4F9),
    onSurfaceVariant = Color(0xFF6F757E),
    outline = Color(0xFF97A0AE),
    outlineVariant = Color(0xFFDADCE1),
    scrim = Color(0x80000000),
    error = Color(0xFFD76B6B)
)

val ColorScheme.divider: Color
    get() = Color(0xFFD0C4C4)

val ColorScheme.success: Color
    get() = Color(0xFF5DC590)

val ColorScheme.blue: Color
    get() = Color(0xFF2F80ED)

val ColorScheme.disabled: Color
    get() = Color(0xFFCBC9C9)

val ColorScheme.onDisabled: Color
    get() = Color(0xFFF8F5F5)

val ColorScheme.surface2: Color
    get() = Color(0xFFC7C9CD)
