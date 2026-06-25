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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun DetailsMessageSheet(
    state: DetailsMessageSheetModel,
    dispatch: (DetailsMessageSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var commentText by rememberSaveable { mutableStateOf("") }
    var textFieldLineCount by remember { mutableIntStateOf(1) }
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
        val inlinedState = state.copy(commentText = commentText)

        Column(
            modifier = Modifier.imePadding()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.DetailsMessageSheetTitle),
                        style = MaterialTheme.typography.livretMedium18
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(DetailsMessageSheetIntent.DismissRequest) }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BasicTextField(
                    value = inlinedState.commentText,
                    onValueChange = { comment -> sheetDispatch(DetailsMessageSheetIntent.CommentChange(comment)) },
                    modifier = Modifier
                        .weight(1F)
                        .heightIn(min = 48.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(if (textFieldLineCount > 1) 16.dp else 50.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    minLines = 1,
                    maxLines = 5,
                    onTextLayout = { textLayoutResult ->
                        textFieldLineCount = textLayoutResult.lineCount
                    },
                    textStyle = MaterialTheme.typography.regular15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 19.sp,
                        letterSpacing = .2.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (inlinedState.commentText.isEmpty()) {
                                Text(
                                    text = stringResource(ClientStrings.DetailsMessageCommentPlaceholder),
                                    style = MaterialTheme.typography.regular15.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 19.sp,
                                        letterSpacing = .2.sp
                                    )
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
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { sheetDispatch(DetailsMessageSheetIntent.SendClick(inlinedState.commentText)) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Message24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun DetailsMessageSheetPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DetailsMessageSheet(
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
}
