@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.checkout_sbp_bank_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.checkout_sbp_bank_sheet.intent.CheckoutSbpBankSheetIntent
import ru.mercury.vpclient.features.checkout_sbp_bank_sheet.model.CheckoutSbpBankSheetModel
import ru.mercury.vpclient.shared.data.entity.CheckoutSbpBank
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CheckoutSbpBankSheet(
    state: CheckoutSbpBankSheetModel,
    dispatch: (CheckoutSbpBankSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CheckoutSbpBankSheetIntent) -> Unit = { intent ->
        when (intent) {
            is CheckoutSbpBankSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            else -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CheckoutSbpBankSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.CheckoutSbpBankSheetTitle),
                        style = MaterialTheme.typography.medium18
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(CheckoutSbpBankSheetIntent.DismissRequest) }
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
                    .weight(weight = 1F, fill = false)
            ) {
                items(
                    items = state.banks,
                    key = { bank -> bank.packageName }
                ) { bank ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { sheetDispatch(CheckoutSbpBankSheetIntent.BankClick(bank)) }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ClientAsyncImage(
                            imageUrl = bank.logoUrl,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Text(
                            text = bank.bankName,
                            modifier = Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.regular15.copy(
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                }
            }

            Text(
                text = stringResource(ClientStrings.CheckoutSbpBankSheetSubtitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CheckoutSbpBankSheetPreview(
    @PreviewParameter(CheckoutSbpBankSheetModelProvider::class) state: CheckoutSbpBankSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CheckoutSbpBankSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CheckoutSbpBankSheetModelProvider: PreviewParameterProvider<CheckoutSbpBankSheetModel> {
    override val values: Sequence<CheckoutSbpBankSheetModel> = sequenceOf(
        CheckoutSbpBankSheetModel(
            banks = listOf(
                CheckoutSbpBank(
                    bankName = "Сбербанк",
                    logoUrl = "",
                    packageName = "ru.sberbankmobile"
                ),
                CheckoutSbpBank(
                    bankName = "Т-Банк",
                    logoUrl = "",
                    packageName = "com.idamob.tinkoff.android"
                ),
                CheckoutSbpBank(
                    bankName = "АЛЬФА-БАНК",
                    logoUrl = "",
                    packageName = "ru.alfabank.mobile.android"
                )
            )
        )
    )
}
