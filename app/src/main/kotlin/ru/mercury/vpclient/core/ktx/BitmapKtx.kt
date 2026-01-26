package ru.mercury.vpclient.core.ktx

import android.graphics.Bitmap
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

val String.bitmap: Bitmap
    @Composable get() {
        val writer = MultiFormatWriter()
        val bitMatrix: BitMatrix = writer.encode(this, BarcodeFormat.CODE_128, 600, 200)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap[x, y] = if (bitMatrix.get(x, y)) Color.Black.toArgb() else MaterialTheme.colorScheme.surface.toArgb()
            }
        }
        return bitmap
    }
