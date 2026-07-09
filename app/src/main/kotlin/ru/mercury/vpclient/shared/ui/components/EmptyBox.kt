package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.VipPlatinumBagEmptyVersion2
import ru.mercury.vpclient.shared.ui.icons.VipPlatinumEmpty
import ru.mercury.vpclient.shared.ui.icons.VipPlatinumFavoriteBrands
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular15

data class EmptyBoxState(
    val imageVector: ImageVector,
    val text: String,
    val buttonText: String? = null,
    val onButtonClick: () -> Unit = {}
)

@Composable
fun EmptyBox(
    state: EmptyBoxState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        Image(
            imageVector = state.imageVector,
            contentDescription = null,
            modifier = Modifier.wrapContentSize()
        )

        Text(
            text = state.text,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 19.sp,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Center
            )
        )

        state.buttonText?.let { buttonText ->
            OutlinedButton(
                onClick = state.onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun EmptyBoxPreview(
    @PreviewParameter(EmptyBoxStateProvider::class) state: EmptyBoxState
) {
    EmptyBox(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
    )
}

private class EmptyBoxStateProvider: PreviewParameterProvider<EmptyBoxState> {

    override val values: Sequence<EmptyBoxState> = sequenceOf(
        EmptyBoxState(
            imageVector = VipPlatinumFavoriteBrands,
            text = "У вас пока нет любимых брендов"
        ),
        EmptyBoxState(
            imageVector = VipPlatinumBagEmptyVersion2,
            text = "У вас пока нет покупок"
        ),
        EmptyBoxState(
            imageVector = VipPlatinumEmpty,
            text = "У вас пока нет подборок"
        ),
        EmptyBoxState(
            imageVector = VipPlatinumEmpty,
            text = "По вашему запросу ничего не найдено",
            buttonText = "Вернуться назад"
        )
    )
}
