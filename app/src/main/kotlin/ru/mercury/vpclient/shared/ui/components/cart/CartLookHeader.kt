package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.shared.ui.components.SharedTextButton
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.livretMedium21
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun CartLookHeader(
    name: String,
    imageUrl: String?,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val (title, button, image, divider) = createRefs()

        Text(
            text = name,
            modifier = Modifier.constrainAs(title) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, 24.dp)
                top.linkTo(parent.top, 16.dp)
                end.linkTo(image.start, 16.dp)
            },
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            style = MaterialTheme.typography.livretMedium21.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 30.sp
            )
        )

        SharedTextButton(
            onClick = onAddClick,
            text = stringResource(ClientStrings.CartLookAdd),
            textStyle = MaterialTheme.typography.medium15.copy(
                color = MaterialTheme.colorScheme.error,
                letterSpacing = .3.sp
            ),
            modifier = Modifier.constrainAs(button) {
                width = Dimension.wrapContent
                start.linkTo(parent.start, 8.dp)
                top.linkTo(title.bottom, 8.dp)
            }
        )

        ClientAsyncImage(
            imageUrl = imageUrl.orEmpty(),
            contentScale = ContentScale.Fit,
            modifier = Modifier.constrainAs(image) {
                width = Dimension.value(96.dp)
                height = Dimension.value(128.dp)
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 24.dp)
                bottom.linkTo(parent.bottom, 16.dp)
            }
        )

        HorizontalDivider(
            modifier = Modifier.constrainAs(divider) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom)
            },
            color = MaterialTheme.colorScheme.divider
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartLookHeaderPreview() {
    CartLookHeader(
        name = "Evening look",
        imageUrl = null,
        onAddClick = {}
    )
}
