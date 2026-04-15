package ru.mercury.vpclient.shared.ui.preview.annotation

import androidx.compose.ui.tooling.preview.Preview
import ru.mercury.vpclient.shared.data.MAX_FONT_SCALE

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
