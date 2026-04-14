package ru.mercury.vpclient.shared.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF1F1F1F)
val OnPrimary = Color(0xFFFFFFFF)
val Secondary = Color(0xFF666061)
val Secondary3 = Color(0xFFAFAFAF)
val Secondary4 = Color(0xFF97A0AE)
val Secondary5 = Color(0xFFDADCE1)
val Secondary6 = Color(0xFF6F757E)
val Background = Color(0xFFFFFFFF)
val OnBackground = Color(0xFF1F1F1F)
val Surface = Color(0xFFF6F3F3)
val Surface3 = Color(0xFFF5F4F9)
val Surface2 = Color(0xFFDCD2D2)
val Surface4 = Color(0xFFEDE7E7)
val OnSurfaceVariant = Color(0xFF666161)
val Error = Color(0xFFD76B6B)
val Divider = Color(0xFFD0C4C4)
val Divider2 = Color(0xFFCAC4D0)
val Green = Color(0xFF5DC590)
val Black1 = Color(0xFF1B1B1B)
val Black50 = Color(0x80000000)
val Disabled = Color(0xFFCBC9C9)
val OnDisabled = Color(0xFFF8F5F5)

val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurfaceVariant = OnSurfaceVariant,
    error = Error
)

val ColorScheme.secondary3: Color
    get() = Secondary3

val ColorScheme.secondary4: Color
    get() = Secondary4

val ColorScheme.secondary5: Color
    get() = Secondary5

val ColorScheme.secondary6: Color
    get() = Secondary6

val ColorScheme.surface2: Color
    get() = Surface2

val ColorScheme.surface3: Color
    get() = Surface3

val ColorScheme.surface4: Color
    get() = Surface4

val ColorScheme.divider: Color
    get() = Divider

val ColorScheme.divider2: Color
    get() = Divider2

val ColorScheme.green: Color
    get() = Green

val ColorScheme.black1: Color
    get() = Black1

val ColorScheme.black50: Color
    get() = Black50

val ColorScheme.disabled: Color
    get() = Disabled

val ColorScheme.onDisabled: Color
    get() = OnDisabled
