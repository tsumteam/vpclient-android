package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.disabled
import ru.mercury.vpclient.core.ui.theme.medium16
import ru.mercury.vpclient.core.ui.theme.onDisabled

@Composable
fun ClientButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.medium16.copy(textAlign = TextAlign.Center)
        )
    }
}

@FontScalePreviews
@Composable
private fun ClientButtonPreview() {
    ClientTheme {
        ClientButton(
            onClick = {},
            text = "Данные доставки",
            modifier = Modifier.padding(16.dp)
        )
    }
}



@Composable
fun ClientButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textAutoSize: TextAutoSize? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.disabled,
            disabledContentColor = MaterialTheme.colorScheme.onDisabled
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.medium16.copy(textAlign = TextAlign.Center),
            maxLines = 1,
            autoSize = textAutoSize
        )
    }
}

@FontScalePreviews
@Composable
private fun ClientButtonPreview2() {
    ClientTheme {
        ClientButton(
            onClick = {},
            text = "Данные доставки",
            enabled = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}



@Composable
fun ClientButton(
    onClick: () -> Unit,
    text: String,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = !loading,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        when {
            loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            }
            else -> {
                Text(
                    text = text,
                    style = MaterialTheme.typography.medium16.copy(textAlign = TextAlign.Center)
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun ClientButtonPreview3() {
    ClientTheme {
        ClientButton(
            onClick = {},
            text = "Войти",
            loading = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}



@Composable
fun ClientButton(
    onClick: () -> Unit,
    text: String,
    loading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val isEnabled = enabled && !loading
    val isLoading = enabled || loading

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.disabled,
            contentColor = if (isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onDisabled,
            disabledContainerColor = if (isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.disabled,
            disabledContentColor = if (isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onDisabled
        )
    ) {
        when {
            loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            }
            else -> {
                Text(
                    text = text,
                    style = MaterialTheme.typography.medium16.copy(textAlign = TextAlign.Center)
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun ClientButtonPreview4() {
    ClientTheme {
        ClientButton(
            onClick = {},
            text = "Войти",
            loading = false,
            modifier = Modifier.padding(16.dp),
            enabled = false
        )
    }
}



@Composable
fun ClientResizeButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.medium16.copy(textAlign = TextAlign.Center)
        )
    }
}

@FontScalePreviews
@Composable
private fun ClientResizeButtonPreview() {
    ClientTheme {
        ClientResizeButton(
            onClick = {},
            text = "Обновить",
            modifier = Modifier
                .padding(16.dp)
                .wrapContentWidth()
                .height(40.dp)
        )
    }
}
