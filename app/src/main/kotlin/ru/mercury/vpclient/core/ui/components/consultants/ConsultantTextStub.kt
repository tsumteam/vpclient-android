package ru.mercury.vpclient.core.ui.components.consultants

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun ConsultantTextStub(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(color = Color.Transparent)
    )
}
