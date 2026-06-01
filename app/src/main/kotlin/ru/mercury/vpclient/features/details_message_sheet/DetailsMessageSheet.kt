@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.details_message_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.details_message_sheet.intent.DetailsMessageSheetIntent
import ru.mercury.vpclient.features.details_message_sheet.model.DetailsMessageSheetModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.details.DetailsMessageProductCard
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.Message24
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun DetailsMessageSheet(
    state: DetailsMessageSheetModel,
    dispatch: (DetailsMessageSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var commentText by rememberSaveable { mutableStateOf("") }

    val sheetDispatch: (DetailsMessageSheetIntent) -> Unit = { intent ->
        when (intent) {
            is DetailsMessageSheetIntent.CommentChange -> commentText = intent.comment
            is DetailsMessageSheetIntent.SendClick -> dispatch(intent)
            is DetailsMessageSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(DetailsMessageSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        DetailsMessageSheetContent(
            state = state.copy(commentText = commentText),
            dispatch = sheetDispatch
        )
    }
}

@Composable
private fun DetailsMessageSheetContent(
    state: DetailsMessageSheetModel,
    dispatch: (DetailsMessageSheetIntent) -> Unit
) {
    Column(
        modifier = Modifier.imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            IconButton(
                onClick = { dispatch(DetailsMessageSheetIntent.DismissRequest) },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Close24,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = stringResource(ClientStrings.DetailsMessageSheetTitle),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 56.dp),
                style = MaterialTheme.typography.livretMedium19.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        DetailsMessageProductCard(
            entity = state.productEntity
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val commentTextStyle = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 19.sp,
                letterSpacing = .2.sp
            )

            BasicTextField(
                value = state.commentText,
                onValueChange = { comment -> dispatch(DetailsMessageSheetIntent.CommentChange(comment)) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(start = 20.dp, end = 20.dp),
                singleLine = true,
                textStyle = commentTextStyle,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.commentText.isEmpty()) {
                            Text(
                                text = stringResource(ClientStrings.DetailsMessageCommentPlaceholder),
                                style = commentTextStyle
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1F1F1F))
                    .clickable { dispatch(DetailsMessageSheetIntent.SendClick(state.commentText)) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Message24,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun DetailsMessageSheetContentPreview() {
    DetailsMessageSheetContent(
        state = DetailsMessageSheetModel(
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
        ),
        dispatch = {}
    )
}
