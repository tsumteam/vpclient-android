@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.compilation_benefit_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.compilation_benefit_sheet.intent.CompilationBenefitSheetIntent
import ru.mercury.vpclient.features.compilation_benefit_sheet.model.CompilationBenefitSheetModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.compilations.CompilationBenefitProductRow
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CompilationBenefitSheet(
    state: CompilationBenefitSheetModel,
    dispatch: (CompilationBenefitSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CompilationBenefitSheetIntent) -> Unit = { intent ->
        scope.launch {
            sheetState.hide()
            dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CompilationBenefitSheetIntent.DismissRequest) },
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding(),
        sheetState = sheetState
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.CompilationBenefitSheetTitle),
                        style = MaterialTheme.typography.livretMedium18
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(CompilationBenefitSheetIntent.DismissRequest) }
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

            SharedLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                state.productEntities.forEach { entity ->
                    item {
                        CompilationBenefitProductRow(
                            entity = entity
                        )
                    }
                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(ClientStrings.CompilationBenefitSheetFullPrice, state.fullPriceText),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        Text(
                            text = stringResource(ClientStrings.CompilationBenefitSheetDiscountPrice, state.discountPriceText),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        Text(
                            text = stringResource(ClientStrings.CompilationBenefitSheetBenefit, state.benefitText),
                            style = MaterialTheme.typography.medium14.copy(
                                color = MaterialTheme.colorScheme.error,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CompilationBenefitSheetPreview(
    @PreviewParameter(CompilationBenefitSheetModelProvider::class) state: CompilationBenefitSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CompilationBenefitSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CompilationBenefitSheetModelProvider: PreviewParameterProvider<CompilationBenefitSheetModel> {
    override val values: Sequence<CompilationBenefitSheetModel> = sequenceOf(
        CompilationBenefitSheetModel(
            productEntities = listOf(
                CatalogFilterProductsEntity.Empty.copy(
                    id = "preview-1",
                    itemId = "79393030",
                    colorId = "Черный",
                    brand = "DOLCE&GABBANA",
                    name = "Пиджак",
                    price = 1_000_000.0,
                    priceWithoutDiscount = 1_600_000.0,
                    imageUrl = "",
                    lookActionPrice = 1_000_000.0,
                    lookActionPriceWithoutDiscount = 1_600_000.0,
                    lookActionName = "Black Friday",
                    lookActionDiscountPercentage = 38
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    id = "preview-2",
                    itemId = "79393030",
                    colorId = "Голубой",
                    brand = "BALMAIN",
                    name = "Платье",
                    price = 2_000_000.0,
                    priceWithoutDiscount = 2_000_000.0,
                    imageUrl = ""
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    id = "preview-3",
                    itemId = "79393030",
                    colorId = "Золотой",
                    brand = "MVST",
                    name = "Серьги",
                    price = 1_000_000.0,
                    priceWithoutDiscount = 1_000_000.0,
                    imageUrl = ""
                )
            )
        )
    )
}
