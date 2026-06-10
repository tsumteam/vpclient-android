package ru.mercury.vpclient.shared.ui.components.profile

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun ProfileQrImage(
    qrCode: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val foregroundColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val bitmap = remember(
        qrCode,
        backgroundColor,
        foregroundColor
    ) {
        when {
            qrCode.isNotEmpty() -> {
                val bitMatrix = MultiFormatWriter().encode(
                    qrCode,
                    BarcodeFormat.QR_CODE,
                    600,
                    600
                )
                val qrBitmap = createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)
                for (x in 0 until bitMatrix.width) {
                    for (y in 0 until bitMatrix.height) {
                        qrBitmap[x, y] = when {
                            bitMatrix.get(x, y) -> foregroundColor
                            else -> backgroundColor
                        }
                    }
                }
                qrBitmap
            }
            else -> createBitmap(1, 1, Bitmap.Config.RGB_565).apply {
                setPixel(0, 0, backgroundColor)
            }
        }
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileQrImagePreview() {
    ProfileQrImage(
        qrCode = "1234567890",
        modifier = Modifier.size(240.dp)
    )
}
