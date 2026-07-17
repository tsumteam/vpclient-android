@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.gift_card_terms_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.gift_card_terms_sheet.intent.GiftCardTermsIntent
import ru.mercury.vpclient.features.gift_card_terms_sheet.model.GiftCardTermsModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun GiftCardTermsSheet(
    state: GiftCardTermsModel,
    dispatch: (GiftCardTermsIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val dismiss: () -> Unit = {
        scope.launch {
            sheetState.hide()
            dispatch(GiftCardTermsIntent.DismissRequest)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState
    ) {
        SharedLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.GiftCardTermsTitle),
                            style = MaterialTheme.typography.livretMedium18
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = dismiss
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
            }
            item {
                Text(
                    text = state.text,
                    modifier = Modifier.padding(horizontal = 16.dp),
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
@Preview
@Composable
private fun GiftCardTermsSheetPreview(
    @PreviewParameter(GiftCardTermsSheetModelPreviewParameterProvider::class) state: GiftCardTermsModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GiftCardTermsSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class GiftCardTermsSheetModelPreviewParameterProvider: PreviewParameterProvider<GiftCardTermsModel> {
    override val values: Sequence<GiftCardTermsModel> = sequenceOf(
        GiftCardTermsModel(
            text = "После внесения предоплаты на Ваш номер телефона придет sms с PIN-кодом, а на почту – письмо с номером карты и штрихкодом. Сразу после этого карту можно использовать. Вы можете воспользоваться картой в течение года с момента активации. Подробнее об условиях использования – в регламенте."
        )
    )
}
