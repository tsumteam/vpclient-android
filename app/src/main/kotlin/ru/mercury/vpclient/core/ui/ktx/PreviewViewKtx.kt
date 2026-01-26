package ru.mercury.vpclient.core.ui.ktx

import android.graphics.Rect
import androidx.camera.view.PreviewView

fun PreviewView.toSensorRect(
    viewRect: Rect,
    imageWidth: Int,
    imageHeight: Int
): Rect {
    val xScale = imageWidth.toFloat() / width
    val yScale = imageHeight.toFloat() / height

    val left = (viewRect.left * xScale).toInt()
    val top = (viewRect.top * yScale).toInt()
    val right = (viewRect.right * xScale).toInt()
    val bottom = (viewRect.bottom * yScale).toInt()

    return Rect(left, top, right, bottom)
}
