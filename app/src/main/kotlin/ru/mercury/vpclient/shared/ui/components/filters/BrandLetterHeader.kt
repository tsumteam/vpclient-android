package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium19

@Composable
fun BrandLetterHeader(
    letter: String
) {
    Text(
        text = letter,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        style = MaterialTheme.typography.livretMedium19.copy(
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 26.sp,
            letterSpacing = .2.sp
        )
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun BrandLetterHeaderPreview() {
    BrandLetterHeader(
        letter = "A"
    )
}
