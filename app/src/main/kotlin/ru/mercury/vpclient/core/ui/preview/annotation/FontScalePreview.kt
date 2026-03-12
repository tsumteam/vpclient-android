package ru.mercury.vpclient.core.ui.preview.annotation

import androidx.compose.ui.tooling.preview.Preview
import ru.mercury.vpclient.core.MAX_FONT_SCALE

@Preview(
    name = "Font 100%",
    showBackground = true
)
@Preview(
    name = "Font 115%",
    fontScale = MAX_FONT_SCALE,
    showBackground = true
)
annotation class FontScalePreviews
