@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.details_message_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.details_message_sheet.intent.DetailsMessageIntent
import ru.mercury.vpclient.features.details_message_sheet.model.DetailsMessageModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.ui.components.SharedColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.details.DetailsMessageProductCard
import ru.mercury.vpclient.shared.ui.components.message.MessageInput
import ru.mercury.vpclient.shared.ui.components.message.MessageInputState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium17

@Composable
fun DetailsMessageSheet(
    state: DetailsMessageModel,
    dispatch: (DetailsMessageIntent) -> Unit
) {
    var commentText by rememberSaveable { mutableStateOf("") }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(DetailsMessageIntent.DismissClick) }
    ) {
        val inlinedState = state.copy(commentText = commentText)

        SharedColumn(
            modifier = Modifier.imePadding()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.DetailsMessageSheetTitle),
                        style = MaterialTheme.typography.livretMedium17.copy(
                            lineHeight = 26.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(DetailsMessageIntent.DismissClick) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )

            DetailsMessageProductCard(
                entity = inlinedState.productEntity
            )

            MessageInput(
                state = MessageInputState(
                    commentText = inlinedState.commentText,
                    onCommentChange = { comment -> commentText = comment },
                    onSendClick = { dispatch(DetailsMessageIntent.SendClick(inlinedState.commentText)) }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun DetailsMessageSheetPreview(
    @PreviewParameter(DetailsMessageModelPreviewParameterProvider::class) state: DetailsMessageModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DetailsMessageSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class DetailsMessageModelPreviewParameterProvider: PreviewParameterProvider<DetailsMessageModel> {
    override val values: Sequence<DetailsMessageModel> = sequenceOf(
        DetailsMessageModel(
            productEntity = ProductEntity.Empty.copy(
                id = "preview",
                name = "Платье миди с поясом",
                itemId = "123456",
                brand = "BRUNELLO CUCINELLI",
                colorName = "Молочный",
                shortDescription = "Платье миди с поясом",
                price = 189_900.0,
                priceWithoutDiscount = 234_900.0
            )
        )
    )
}
