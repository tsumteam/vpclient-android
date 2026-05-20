package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.Edit24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.cartSwipeEdit
import ru.mercury.vpclient.shared.ui.theme.regular12

@Composable
fun CartProductSwipeAction(
    imageVector: ImageVector?,
    text: String,
    backgroundColor: Color,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier,
    width: Dp = 88.dp
) {
    Column(
        modifier = modifier
            .wrapContentWidth(align = Alignment.Start, unbounded = true)
            .requiredWidth(width)
            .fillMaxHeight()
            .background(backgroundColor),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageVector?.let { vector ->
            Icon(
                imageVector = vector,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = contentColor
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.regular12.copy(
                color = contentColor,
                lineHeight = 16.sp,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductSwipeActionPreview() {
    CartProductSwipeAction(
        imageVector = Edit24,
        text = "Изменить",
        backgroundColor = MaterialTheme.colorScheme.cartSwipeEdit,
        modifier = Modifier
            .height(178.dp),
        width = 88.dp
    )
}
