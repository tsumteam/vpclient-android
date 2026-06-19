@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.details_cart_added_sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.details_cart_added_sheet.intent.DetailsCartAddedSheetIntent
import ru.mercury.vpclient.features.details_cart_added_sheet.model.DetailsCartAddedSheetModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.details.DetailsMessageProductCard
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun DetailsCartAddedSheet(
    state: DetailsCartAddedSheetModel,
    dispatch: (DetailsCartAddedSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (DetailsCartAddedSheetIntent) -> Unit = { intent ->
        scope.launch {
            sheetState.hide()
            dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(DetailsCartAddedSheetIntent.DismissRequest) },
        sheetState = sheetState,
        containerColor = Color.Transparent
    ) {
        SharedLazyColumn(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background), 
            contentPadding = PaddingValues(
                bottom = 16.dp
            )
        ) {
            item {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.DetailsCartAddedSheetTitle),
                            style = MaterialTheme.typography.livretMedium18.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
            item {
                DetailsMessageProductCard(
                    entity = state.productEntity
                )
            }
            item {
                OutlinedButton(
                    onClick = { sheetDispatch(DetailsCartAddedSheetIntent.ContinueShoppingClick) },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.DetailsCartAddedSheetContinueShopping),
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
                        )
                    )
                }
            }
            item {
                Button(
                    onClick = { sheetDispatch(DetailsCartAddedSheetIntent.CartClick) },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.DetailsCartAddedSheetCart),
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
                        )
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun DetailsCartAddedSheetPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DetailsCartAddedSheet(
            state = DetailsCartAddedSheetModel(
                productEntity = ProductEntity.Empty.copy(
                    id = "preview",
                    name = "Пиджак",
                    itemId = "79393030",
                    brand = "DOLCE&GABBANA",
                    colorName = "Черный",
                    shortDescription = "Пиджак",
                    price = 1_600_000.0,
                    colorImageUrls = listOf("")
                )
            ),
            dispatch = {}
        )
    }
}
