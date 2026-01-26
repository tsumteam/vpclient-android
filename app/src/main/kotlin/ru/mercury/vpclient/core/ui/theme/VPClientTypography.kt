package ru.mercury.vpclient.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object VPClientTypography {

    private val LocalTextStyle: TextStyle
        get() = TextStyle()

    private val Regular: TextStyle
        get() = LocalTextStyle.copy(fontWeight = FontWeight.W400)

    private val Regular_12: TextStyle
        get() = Regular.copy(fontSize = 12.sp)

    val Regular_12_Secondary: TextStyle
        @Composable get() = Regular_12.copy(color = MaterialTheme.colorScheme.secondary)

    val Regular_14: TextStyle
        get() = Regular.copy(fontSize = 14.sp)

    val Regular_14_OnBackground: TextStyle
        @Composable get() = Regular_14.copy(color = MaterialTheme.colorScheme.onBackground)

    val Regular_14_Secondary: TextStyle
        @Composable get() = Regular_14.copy(color = MaterialTheme.colorScheme.secondary)

    val Regular_14_Error: TextStyle
        @Composable get() = Regular_14.copy(color = MaterialTheme.colorScheme.error)

    private val Regular_15: TextStyle
        get() = Regular.copy(fontSize = 15.sp)

    val Regular_15_Secondary: TextStyle
        @Composable get() = Regular_15.copy(color = MaterialTheme.colorScheme.secondary)

    val Regular_16: TextStyle
        get() = Regular.copy(fontSize = 16.sp)

    val Regular_16_OnBackground: TextStyle
        @Composable get() = Regular_16.copy(color = MaterialTheme.colorScheme.onBackground)

    private val Regular_18: TextStyle
        get() = Regular.copy(fontSize = 18.sp)

    val Regular_18_OnBackground: TextStyle
        @Composable get() = Regular_18.copy(color = MaterialTheme.colorScheme.onBackground)

    private val Regular_22: TextStyle
        get() = Regular.copy(fontSize = 22.sp)

    val Regular_22_OnBackground: TextStyle
        @Composable get() = Regular_22.copy(color = MaterialTheme.colorScheme.onBackground)

    val Regular_22_Error: TextStyle
        @Composable get() = Regular_22.copy(color = MaterialTheme.colorScheme.error)

    private val Medium: TextStyle
        get() = LocalTextStyle.copy(fontWeight = FontWeight.Medium)

    private val Medium_11: TextStyle
        get() = Medium.copy(fontSize = 11.sp)

    val Medium_11_OnBackground: TextStyle
        @Composable get() = Medium_11.copy(color = MaterialTheme.colorScheme.onBackground)

    private val Medium_12: TextStyle
        get() = Medium.copy(fontSize = 12.sp)

    val Medium_12_OnBackground: TextStyle
        @Composable get() = Medium_12.copy(color = MaterialTheme.colorScheme.onBackground)

    val Medium_12_Secondary: TextStyle
        @Composable get() = Medium_12.copy(color = MaterialTheme.colorScheme.secondary)

    val Medium_12_White: TextStyle
        get() = Medium_12.copy(color = Color.White)

    val Medium_14: TextStyle
        get() = Medium.copy(fontSize = 14.sp)

    val Medium_14_OnBackground: TextStyle
        @Composable get() = Medium_14.copy(color = MaterialTheme.colorScheme.onBackground)

    val Medium_14_White: TextStyle
        get() = Medium_14.copy(color = Color.White)

    val Medium_16: TextStyle
        get() = Medium.copy(fontSize = 16.sp)

    val Medium_16_OnBackground: TextStyle
        @Composable get() = Medium_16.copy(color = MaterialTheme.colorScheme.onBackground)

    private val Medium_17: TextStyle
        get() = Medium.copy(fontSize = 17.sp)

    val Medium_17_White: TextStyle
        get() = Medium_17.copy(color = Color.White)



    private val LocalSpanStyle: SpanStyle
        get() = SpanStyle()

    private val SpanStyle_Regular: SpanStyle
        get() = LocalSpanStyle.copy(fontWeight = FontWeight.W400)

    private val SpanStyle_Regular_12: SpanStyle
        get() = SpanStyle_Regular.copy(fontSize = 12.sp)

    val SpanStyle_Regular_12_Secondary5: SpanStyle
        @Composable get() = SpanStyle_Regular_12.copy(color = MaterialTheme.colorScheme.secondary5)

    private val SpanStyle_Regular_14: SpanStyle
        get() = SpanStyle_Regular.copy(fontSize = 14.sp)

    val SpanStyle_Regular_14_OnBackground: SpanStyle
        @Composable get() = SpanStyle_Regular_14.copy(color = MaterialTheme.colorScheme.onBackground)

    val SpanStyle_Regular_14_Error: SpanStyle
        @Composable get() = SpanStyle_Regular_14.copy(color = MaterialTheme.colorScheme.error)

    val SpanStyle_Regular_14_Secondary3: SpanStyle
        @Composable get() = SpanStyle_Regular_14.copy(color = MaterialTheme.colorScheme.secondary3)

    private val SpanStyle_Medium: SpanStyle
        get() = LocalSpanStyle.copy(fontWeight = FontWeight.Medium)

    private val SpanStyle_Medium_14: SpanStyle
        get() = SpanStyle_Medium.copy(fontSize = 14.sp)

    val SpanStyle_Medium_14_OnBackground: SpanStyle
        @Composable get() = SpanStyle_Medium_14.copy(color = MaterialTheme.colorScheme.onBackground)

    val SpanStyle_Medium_14_Secondary: SpanStyle
        @Composable get() = SpanStyle_Medium_14.copy(color = MaterialTheme.colorScheme.secondary)

    private val SpanStyle_Medium_17: SpanStyle
        get() = SpanStyle_Medium.copy(fontSize = 17.sp)

    val SpanStyle_Medium_17_White: SpanStyle
        get() = SpanStyle_Medium_17.copy(color = Color.White)
}
