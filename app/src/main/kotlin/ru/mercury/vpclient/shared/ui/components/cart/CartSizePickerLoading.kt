package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

@Composable
fun CartSizePickerLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(56.dp)
                    .height(16.dp)
                    .placeholder(shape = RoundedCornerShape(4.dp))
            )

            Spacer(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(116.dp)
                    .height(16.dp)
                    .placeholder(shape = RoundedCornerShape(4.dp))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(50.dp)
                    .height(58.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .width(54.dp)
                        .height(16.dp)
                        .placeholder(shape = RoundedCornerShape(4.dp))
                )

                Spacer(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .width(54.dp)
                        .height(16.dp)
                        .placeholder(shape = RoundedCornerShape(4.dp))
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(start = 8.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(
                    count = 5
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(width = 54.dp, height = 62.dp)
                            .placeholder(shape = RoundedCornerShape(4.dp))
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
                .width(112.dp)
                .height(16.dp)
                .placeholder(shape = RoundedCornerShape(4.dp))
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartSizePickerLoadingPreview() {
    CartSizePickerLoading()
}
