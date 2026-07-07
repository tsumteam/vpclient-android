package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.BasketFilled24
import ru.mercury.vpclient.shared.ui.icons.Delete24
import ru.mercury.vpclient.shared.ui.icons.Disassemble24
import ru.mercury.vpclient.shared.ui.icons.Edit24
import ru.mercury.vpclient.shared.ui.icons.ReturnToBasket24
import ru.mercury.vpclient.shared.ui.icons.SendFilled24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.LightColorScheme
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.surface2

data class SwipeActionBoxState(
    val imageVector: ImageVector?,
    val text: String,
    val backgroundColor: Color,
    val contentColor: Color,
    val width: Dp = 88.dp,
    val contentHorizontalAlignment: Alignment.Horizontal = Alignment.Start,
    val onClick: () -> Unit = {}
)

@Composable
fun SwipeActionBox(
    state: SwipeActionBoxState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .wrapContentWidth(align = state.contentHorizontalAlignment, unbounded = true)
            .requiredWidth(state.width)
            .fillMaxHeight()
            .background(state.backgroundColor)
            .clickable(onClick = state.onClick),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.imageVector?.let { vector ->
            Icon(
                imageVector = vector,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = state.contentColor
            )
        }

        Text(
            text = state.text,
            style = MaterialTheme.typography.regular12.copy(
                color = state.contentColor,
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
private fun SwipeActionBoxPreview(
    @PreviewParameter(SwipeActionBoxStateProvider::class) state: SwipeActionBoxState
) {
    SwipeActionBox(
        state = state,
        modifier = Modifier.height(178.dp)
    )
}

private class SwipeActionBoxStateProvider: PreviewParameterProvider<SwipeActionBoxState> {
    override val values: Sequence<SwipeActionBoxState> = sequenceOf(
        SwipeActionBoxState(
            imageVector = Edit24,
            text = "Изменить",
            backgroundColor = LightColorScheme.surface2,
            contentColor = LightColorScheme.onPrimary
        ),
        SwipeActionBoxState(
            imageVector = BasketFilled24,
            text = "Отделить\nот образа",
            backgroundColor = LightColorScheme.secondary,
            contentColor = LightColorScheme.onPrimary
        ),
        SwipeActionBoxState(
            imageVector = Delete24,
            text = "Удалить",
            backgroundColor = LightColorScheme.error,
            contentColor = LightColorScheme.onPrimary
        ),
        SwipeActionBoxState(
            imageVector = null,
            text = "Вернуть\nоригинал",
            backgroundColor = LightColorScheme.outline,
            contentColor = LightColorScheme.onPrimary,
            contentHorizontalAlignment = Alignment.End
        ),
        SwipeActionBoxState(
            imageVector = null,
            text = "Показать\nсписок\nальтернатив",
            backgroundColor = LightColorScheme.surfaceContainerHigh,
            contentColor = LightColorScheme.onBackground,
            contentHorizontalAlignment = Alignment.End
        ),
        SwipeActionBoxState(
            imageVector = null,
            text = "Скрыть\nсписок\nальтернатив",
            backgroundColor = LightColorScheme.surfaceContainerHigh,
            contentColor = LightColorScheme.onBackground,
            contentHorizontalAlignment = Alignment.End
        ),
        SwipeActionBoxState(
            imageVector = ReturnToBasket24,
            text = "Вернуть\nв корзину",
            backgroundColor = LightColorScheme.primary,
            contentColor = LightColorScheme.onPrimary
        ),
        SwipeActionBoxState(
            imageVector = Disassemble24,
            text = "Разобрать",
            backgroundColor = LightColorScheme.surface2,
            contentColor = LightColorScheme.onPrimary
        ),
        SwipeActionBoxState(
            imageVector = SendFilled24,
            text = "Обсудить\nв чате",
            backgroundColor = LightColorScheme.outline,
            contentColor = LightColorScheme.onPrimary,
            width = 96.dp
        )
    )
}
