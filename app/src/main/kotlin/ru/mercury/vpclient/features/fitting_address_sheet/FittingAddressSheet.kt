@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_address_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.fitting_address_sheet.intent.FittingAddressSheetIntent
import ru.mercury.vpclient.features.fitting_address_sheet.model.FittingAddressModel
import ru.mercury.vpclient.shared.data.entity.FittingAddressFormField
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun FittingAddressSheet(
    state: FittingAddressModel,
    dispatch: (FittingAddressSheetIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SharedModalBottomSheet(
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding(),
        onDismissRequest = { dispatch(FittingAddressSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        val focusManager = LocalFocusManager.current
        val flatFocusRequester = remember { FocusRequester() }
        val entranceFocusRequester = remember { FocusRequester() }
        val intercomFocusRequester = remember { FocusRequester() }
        val floorFocusRequester = remember { FocusRequester() }
        val commentFocusRequester = remember { FocusRequester() }

        SharedScaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.FittingAddressFormTitle),
                            style = MaterialTheme.typography.livretMedium18.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 26.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { dispatch(FittingAddressSheetIntent.DismissRequest) }
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            floatingActionButton = {
                Button(
                    onClick = { dispatch(FittingAddressSheetIntent.SaveAddressClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = when {
                            state.isEdit -> stringResource(ClientStrings.FittingAddressSave)
                            else -> stringResource(ClientStrings.FittingConfirmationAddAddress)
                        },
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
                        )
                    )
                }
            },
            snackbarHost = {
                SharedSnackbarHost(
                    hostState = snackbarHostStateError,
                    modifier = Modifier.padding(bottom = 16.dp),
                    containerColor = MaterialTheme.colorScheme.error
                )
            }
        ) { innerPadding ->
            SharedLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                contentPadding = innerPadding + PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    when {
                        state.address.isNotBlank() -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                    .clickable { dispatch(FittingAddressSheetIntent.OpenAddressSearch) }
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(ClientStrings.FittingAddressCityStreetHousePlaceholder),
                                    style = MaterialTheme.typography.regular12.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 16.sp,
                                        letterSpacing = .2.sp
                                    )
                                )

                                Text(
                                    text = state.address,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.regular15.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        lineHeight = 19.sp,
                                        letterSpacing = .2.sp
                                    )
                                )
                            }
                        }
                        else -> {
                            Text(
                                text = stringResource(ClientStrings.FittingAddressCityStreetHousePlaceholder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                    .clickable { dispatch(FittingAddressSheetIntent.OpenAddressSearch) }
                                    .padding(horizontal = 16.dp)
                                    .wrapContentHeight(Alignment.CenterVertically),
                                style = MaterialTheme.typography.regular15.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 19.sp,
                                    letterSpacing = .2.sp
                                )
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            value = state.flat,
                            onValueChange = {
                                dispatch(
                                    FittingAddressSheetIntent.AddressFormValueChange(FittingAddressFormField.Flat, it)
                                )
                            },
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxWidth()
                                .height(52.dp)
                                .focusRequester(flatFocusRequester),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            ),
                            label = {
                                Text(
                                    text = stringResource(ClientStrings.FittingAddressFlatOfficeLabel)
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { entranceFocusRequester.requestFocus() }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                cursorColor = MaterialTheme.colorScheme.onBackground,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        TextField(
                            value = state.entrance,
                            onValueChange = {
                                dispatch(
                                    FittingAddressSheetIntent.AddressFormValueChange(
                                        FittingAddressFormField.Entrance,
                                        it
                                    )
                                )
                            },
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxWidth()
                                .height(52.dp)
                                .focusRequester(entranceFocusRequester),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            ),
                            label = {
                                Text(
                                    text = stringResource(ClientStrings.FittingAddressEntranceLabel)
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { intercomFocusRequester.requestFocus() }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                cursorColor = MaterialTheme.colorScheme.onBackground,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            value = state.intercom,
                            onValueChange = {
                                dispatch(
                                    FittingAddressSheetIntent.AddressFormValueChange(
                                        FittingAddressFormField.Intercom,
                                        it
                                    )
                                )
                            },
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxWidth()
                                .height(52.dp)
                                .focusRequester(intercomFocusRequester),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            ),
                            label = {
                                Text(
                                    text = stringResource(ClientStrings.FittingAddressIntercomLabel)
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { floorFocusRequester.requestFocus() }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                cursorColor = MaterialTheme.colorScheme.onBackground,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        TextField(
                            value = state.floor,
                            onValueChange = {
                                dispatch(
                                    FittingAddressSheetIntent.AddressFormValueChange(
                                        FittingAddressFormField.Floor,
                                        it
                                    )
                                )
                            },
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxWidth()
                                .height(52.dp)
                                .focusRequester(floorFocusRequester),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            ),
                            label = {
                                Text(
                                    text = stringResource(ClientStrings.FittingAddressFloorLabel)
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { commentFocusRequester.requestFocus() }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                cursorColor = MaterialTheme.colorScheme.onBackground,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
                item {
                    TextField(
                        value = state.comment,
                        onValueChange = {
                            dispatch(FittingAddressSheetIntent.AddressFormValueChange(FittingAddressFormField.Comment, it))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(102.dp)
                            .focusRequester(commentFocusRequester),
                        textStyle = MaterialTheme.typography.regular15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 19.sp,
                            letterSpacing = .2.sp
                        ),
                        label = {
                            Text(
                                text = stringResource(ClientStrings.FittingAddressCommentPlaceholder)
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                            cursorColor = MaterialTheme.colorScheme.onBackground,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                dispatch(FittingAddressSheetIntent.SaveAddressClick)
                            }
                        ),
                        maxLines = 3
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FittingAddressSheetPreview(
    @PreviewParameter(FittingAddressModelProvider::class) form: FittingAddressModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FittingAddressSheet(
            state = form,
            snackbarHostStateError = remember { SnackbarHostState() },
            dispatch = {}
        )
    }
}

private class FittingAddressModelProvider: PreviewParameterProvider<FittingAddressModel> {
    override val values: Sequence<FittingAddressModel> = sequenceOf(
        FittingAddressModel(),
        FittingAddressModel(
            address = "г. Москва, ул. Тверская, д. 9",
            flat = "14",
            entrance = "2",
            intercom = "835",
            floor = "5",
            comment = "Позвонить за 10 минут до приезда. Домофон может не работать."
        )
    )
}
