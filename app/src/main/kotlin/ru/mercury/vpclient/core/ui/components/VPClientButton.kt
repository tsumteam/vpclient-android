package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography
import ru.mercury.vpclient.core.ui.theme.disabled
import ru.mercury.vpclient.core.ui.theme.onDisabled

@Composable
fun VPClientButton(
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
            textAlign = TextAlign.Center,
            style = VPClientTypography.Medium_16
        )
    }
}

@Preview
@Composable
private fun VPClientButtonPreview() {
    VPClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            VPClientButton(
                onClick = {},
                text = "Данные доставки",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}



@Composable
fun VPClientButton(
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
            textAlign = TextAlign.Center,
            style = VPClientTypography.Medium_16,
            maxLines = 1,
            autoSize = textAutoSize
        )
    }
}

@Preview
@Composable
private fun VPClientButtonPreview2() {
    VPClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            VPClientButton(
                onClick = {},
                text = "Данные доставки",
                enabled = false,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}



@Composable
fun VPClientButton(
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
                    textAlign = TextAlign.Center,
                    style = VPClientTypography.Medium_16
                )
            }
        }
    }
}

@Preview
@Composable
private fun VPClientButtonPreview3() {
    VPClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            VPClientButton(
                onClick = {},
                text = "Войти",
                loading = true,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}



@Composable
fun VPClientButton(
    onClick: () -> Unit,
    text: String,
    loading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled || loading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.disabled,
            contentColor = if (enabled || loading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onDisabled,
            disabledContainerColor = if (enabled || loading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.disabled,
            disabledContentColor = if (enabled || loading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onDisabled
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
                    textAlign = TextAlign.Center,
                    style = VPClientTypography.Medium_16
                )
            }
        }
    }
}

@Preview
@Composable
private fun VPClientButtonPreview4() {
    VPClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            VPClientButton(
                onClick = {},
                text = "Войти",
                loading = false,
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = false
            )
        }
    }
}



@Composable
fun VPClientResizeButton(
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
            textAlign = TextAlign.Center,
            style = VPClientTypography.Medium_16
        )
    }
}

@Preview
@Composable
private fun VPClientResizeButtonPreview() {
    VPClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            VPClientResizeButton(
                onClick = {},
                text = "Обновить",
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)
            )
        }
    }
}
