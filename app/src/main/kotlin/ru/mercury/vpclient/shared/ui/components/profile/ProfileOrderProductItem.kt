package ru.mercury.vpclient.shared.ui.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.ui.components.cart.CartBrandBox
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular14

data class ProfileOrderProductItemState(
    val productId: String,
    val imageUrl: String,
    val brand: String,
    val urlBrandLogo: String?,
    val name: String,
    val color: String,
    val article: String,
    val price: String,
    val size: String,
    val status: String,
    val quantity: Int
)

@Composable
fun ProfileOrderProductItem(
    state: ProfileOrderProductItemState,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(state.productId) }
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ClientAsyncImage(
            imageUrl = state.imageUrl,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(width = 88.dp, height = 131.dp)
        )

        Column(
            modifier = Modifier
                .height(116.dp)
                .weight(1F)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CartBrandBox(
                    entity = BrandEntity(
                        brand = state.brand,
                        urlBrandLogo = state.urlBrandLogo
                    ),
                    modifier = Modifier
                        .weight(1F)
                        .height(24.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    if (state.status.isNotBlank()) {
                        Text(
                            text = state.status,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.error,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }

                    Text(
                        text = state.size,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }

            Text(
                text = state.name,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = state.color,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = stringResource(ClientStrings.CartArticle, state.article),
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Spacer(
                modifier = Modifier.weight(1F)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.price,
                    modifier = Modifier.weight(1F),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.medium14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp
                    )
                )

                Text(
                    text = stringResource(ClientStrings.ProfileOrderProductQuantity, state.quantity),
                    maxLines = 1,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ProfileOrderProductItemPreview() {
    ProfileOrderProductItem(
        state = ProfileOrderProductItemState(
            productId = "7930630",
            imageUrl = "",
            brand = "MVST",
            urlBrandLogo = null,
            name = "Куртка",
            color = "Бежевый",
            article = "7930630",
            price = "100 000 ₽",
            size = "IT 40 | RU 42",
            status = "Не оплачен",
            quantity = 1
        ),
        onClick = {}
    )
}
