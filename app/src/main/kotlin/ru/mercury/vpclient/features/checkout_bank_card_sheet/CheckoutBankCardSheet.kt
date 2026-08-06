@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.checkout_bank_card_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.R
import ru.mercury.vpclient.features.checkout_bank_card_sheet.intent.CheckoutBankCardSheetIntent
import ru.mercury.vpclient.features.checkout_bank_card_sheet.model.CheckoutBankCardPaymentSystem
import ru.mercury.vpclient.features.checkout_bank_card_sheet.model.CheckoutBankCardSheetModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.TextFieldError24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CheckoutBankCardSheet(
    state: CheckoutBankCardSheetModel,
    dispatch: (CheckoutBankCardSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val cardNumberFocusRequester = remember { FocusRequester() }
    val expirationDateFocusRequester = remember { FocusRequester() }
    val cvvFocusRequester = remember { FocusRequester() }
    var cardNumberTextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = state.cardNumber,
                selection = TextRange(index = state.cardNumber.length)
            )
        )
    }
    var expirationDateTextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = state.expirationDate,
                selection = TextRange(index = state.expirationDate.length)
            )
        )
    }
    var isCardNumberEverFocused by remember { mutableStateOf(false) }
    var isExpirationDateEverFocused by remember { mutableStateOf(false) }
    val sheetDispatch: (CheckoutBankCardSheetIntent) -> Unit = { intent ->
        when (intent) {
            is CheckoutBankCardSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            else -> dispatch(intent)
        }
    }

    LaunchedEffect(state.cardNumber) {
        when {
            cardNumberTextFieldValue.text != state.cardNumber -> {
                cardNumberTextFieldValue = TextFieldValue(
                    text = state.cardNumber,
                    selection = TextRange(index = state.cardNumber.length)
                )
            }
        }
    }

    LaunchedEffect(state.expirationDate) {
        when {
            expirationDateTextFieldValue.text != state.expirationDate -> {
                expirationDateTextFieldValue = TextFieldValue(
                    text = state.expirationDate,
                    selection = TextRange(index = state.expirationDate.length)
                )
            }
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CheckoutBankCardSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        SharedLazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.checkout_visa),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(width = 56.dp, height = 40.dp)
                                    .then(
                                        when {
                                            state.paymentSystem == CheckoutBankCardPaymentSystem.Visa -> {
                                                Modifier.border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.error,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                            }
                                            else -> Modifier
                                        }
                                    )
                            )

                            Image(
                                painter = painterResource(R.drawable.checkout_mastercard),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(width = 56.dp, height = 40.dp)
                                    .then(
                                        when {
                                            state.paymentSystem == CheckoutBankCardPaymentSystem.Mastercard -> {
                                                Modifier.border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.error,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                            }
                                            else -> Modifier
                                        }
                                    )
                            )

                            Image(
                                painter = painterResource(R.drawable.checkout_mir),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(width = 56.dp, height = 40.dp)
                                    .then(
                                        when {
                                            state.paymentSystem == CheckoutBankCardPaymentSystem.Mir -> {
                                                Modifier.border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.error,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                            }
                                            else -> Modifier
                                        }
                                    )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { sheetDispatch(CheckoutBankCardSheetIntent.DismissRequest) }
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
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            item {
                Text(
                    text = stringResource(ClientStrings.CheckoutBankCardSheetTitle),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                    style = MaterialTheme.typography.medium18.copy(
                        lineHeight = 18.sp
                    )
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.divider,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BasicTextField(
                        value = cardNumberTextFieldValue,
                        onValueChange = { value ->
                            cardNumberTextFieldValue = value
                            sheetDispatch(CheckoutBankCardSheetIntent.CardNumberChange(value.text))
                        },
                        modifier = Modifier
                            .focusRequester(cardNumberFocusRequester)
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .then(
                                when {
                                    state.isCardNumberErrorVisible -> Modifier.border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.error,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    else -> Modifier
                                }
                            )
                            .onFocusChanged { focusState ->
                                when {
                                    focusState.isFocused -> isCardNumberEverFocused = true
                                    isCardNumberEverFocused -> {
                                        sheetDispatch(CheckoutBankCardSheetIntent.CardNumberFocusLost)
                                    }
                                }
                            },
                        textStyle = MaterialTheme.typography.regular15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 19.sp,
                            letterSpacing = .2.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { expirationDateFocusRequester.requestFocus() }
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.error),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    when {
                                        state.cardNumber.isEmpty() -> {
                                            Text(
                                                text = stringResource(
                                                    ClientStrings.CheckoutBankCardSheetNumberPlaceholder
                                                ),
                                                style = MaterialTheme.typography.regular15.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    lineHeight = 19.sp,
                                                    letterSpacing = .2.sp
                                                )
                                            )
                                        }
                                    }
                                    innerTextField()
                                }
                                if (state.isCardNumberErrorVisible) {
                                    Icon(
                                        imageVector = TextFieldError24,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .size(24.dp),
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BasicTextField(
                            value = expirationDateTextFieldValue,
                            onValueChange = { value ->
                                expirationDateTextFieldValue = value
                                sheetDispatch(CheckoutBankCardSheetIntent.ExpirationDateChange(value.text))
                            },
                            modifier = Modifier
                                .weight(1F)
                                .focusRequester(expirationDateFocusRequester)
                                .fillMaxWidth()
                                .height(52.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .then(
                                    when {
                                        state.isExpirationDateErrorVisible -> Modifier.border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.error,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        else -> Modifier
                                    }
                                )
                                .onFocusChanged { focusState ->
                                    when {
                                        focusState.isFocused -> isExpirationDateEverFocused = true
                                        isExpirationDateEverFocused -> {
                                            sheetDispatch(
                                                CheckoutBankCardSheetIntent.ExpirationDateFocusLost
                                            )
                                        }
                                    }
                                },
                            textStyle = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { cvvFocusRequester.requestFocus() }
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.error),
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1F)
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        when {
                                            state.expirationDate.isEmpty() -> {
                                                Text(
                                                    text = stringResource(
                                                        ClientStrings.CheckoutBankCardSheetExpirationPlaceholder
                                                    ),
                                                    style = MaterialTheme.typography.regular15.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        lineHeight = 19.sp,
                                                        letterSpacing = .2.sp
                                                    )
                                                )
                                            }
                                        }
                                        innerTextField()
                                    }
                                    if (state.isExpirationDateErrorVisible) {
                                        Icon(
                                            imageVector = TextFieldError24,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(end = 16.dp)
                                                .size(24.dp),
                                            tint = Color.Unspecified
                                        )
                                    }
                                }
                            }
                        )

                        BasicTextField(
                            value = state.cvv,
                            onValueChange = { sheetDispatch(CheckoutBankCardSheetIntent.CvvChange(it)) },
                            modifier = Modifier
                                .weight(1F)
                                .focusRequester(cvvFocusRequester)
                                .fillMaxWidth()
                                .height(52.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            textStyle = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.error),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    when {
                                        state.cvv.isEmpty() -> {
                                            Text(
                                                text = stringResource(
                                                    ClientStrings.CheckoutBankCardSheetCvvPlaceholder
                                                ),
                                                style = MaterialTheme.typography.regular15.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    lineHeight = 19.sp,
                                                    letterSpacing = .2.sp
                                                )
                                            )
                                        }
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { sheetDispatch(CheckoutBankCardSheetIntent.SaveCardClick) }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.isSaveCardChecked,
                        onCheckedChange = { sheetDispatch(CheckoutBankCardSheetIntent.SaveCardClick) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.onBackground,
                            checkmarkColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )

                    Text(
                        text = stringResource(ClientStrings.CheckoutBankCardSheetSave),
                        style = MaterialTheme.typography.regular14.copy(
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }
            item {
                Button(
                    onClick = { sheetDispatch(CheckoutBankCardSheetIntent.PayClick) },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = state.isPayEnabled,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = when {
                            state.isLoading -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.disabled
                        },
                        disabledContentColor = when {
                            state.isLoading -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onDisabled
                        }
                    )
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        }
                        else -> {
                            Text(
                                text = stringResource(ClientStrings.CheckoutBankCardSheetPay),
                                style = MaterialTheme.typography.medium15.copy(
                                    letterSpacing = .3.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) { cardNumberFocusRequester.requestFocus() }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CheckoutBankCardSheetPreview(
    @PreviewParameter(CheckoutBankCardSheetModelProvider::class) state: CheckoutBankCardSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CheckoutBankCardSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CheckoutBankCardSheetModelProvider: PreviewParameterProvider<CheckoutBankCardSheetModel> {

    override val values: Sequence<CheckoutBankCardSheetModel> = sequenceOf(
        CheckoutBankCardSheetModel(),
        CheckoutBankCardSheetModel(
            cardNumber = "2204 7262 8765 0025",
            expirationDate = "12/28",
            cvv = "123"
        ),
        CheckoutBankCardSheetModel(
            cardNumber = "2204 7262 8765",
            expirationDate = "12/28",
            cvv = "123",
            isSaveCardChecked = true,
            isCardNumberErrorVisible = true
        ),
        CheckoutBankCardSheetModel(
            cardNumber = "2204 7262 8765 0025",
            expirationDate = "13/20",
            cvv = "123",
            isExpirationDateErrorVisible = true
        )
    )
}
