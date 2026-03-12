package ru.mercury.vpclient.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.R

val Typography = Typography()

val Typography.regular11: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 11.sp
    )

val Typography.regular12: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )

val Typography.regular14: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    )

val Typography.regular15: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 15.sp
    )

val Typography.regular16: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    )

val Typography.regular18: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 18.sp
    )

val Typography.regular22: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 22.sp
    )

val Typography.medium11: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )

val Typography.medium12: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )

val Typography.medium14: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )

val Typography.medium16: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )

val Typography.medium17: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp
    )

val Typography.medium21: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 21.sp
    )

val Typography.medium22: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    )

val Typography.livretRegular14: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = FontFamily(Font(resId = R.font.tt_livret_subhead_regular, weight = FontWeight.W400)),
        fontSize = 14.sp
    )

val Typography.livretRegular16: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W400,
        fontFamily = FontFamily(Font(resId = R.font.tt_livret_subhead_regular, weight = FontWeight.W400)),
        fontSize = 16.sp
    )

val Typography.livretMedium13: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W500,
        fontFamily = FontFamily(Font(resId = R.font.tt_livret_subhead_medium, weight = FontWeight.W500)),
        fontSize = 13.sp
    )

val Typography.livretMedium19: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W500,
        fontFamily = FontFamily(Font(resId = R.font.tt_livret_subhead_medium, weight = FontWeight.W500)),
        fontSize = 19.sp,
        letterSpacing = .2.sp
    )

val Typography.livretMedium21: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.W500,
        fontFamily = FontFamily(Font(resId = R.font.tt_livret_subhead_medium, weight = FontWeight.W500)),
        fontSize = 21.sp,
        letterSpacing = .2.sp
    )

val Typography.spanRegular14: SpanStyle
    get() = SpanStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    )

val Typography.spanMedium14: SpanStyle
    get() = SpanStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )

@Composable
fun TextStyle.onBackground(): TextStyle {
    return copy(color = MaterialTheme.colorScheme.onBackground)
}

@Composable
fun TextStyle.secondary(): TextStyle {
    return copy(color = MaterialTheme.colorScheme.secondary)
}

@Composable
fun TextStyle.error(): TextStyle {
    return copy(color = MaterialTheme.colorScheme.error)
}

@Composable
fun TextStyle.white(): TextStyle {
    return copy(color = Color.White)
}

@Composable
fun SpanStyle.onBackground(): SpanStyle {
    return copy(color = MaterialTheme.colorScheme.onBackground)
}

@Composable
fun SpanStyle.secondary(): SpanStyle {
    return copy(color = MaterialTheme.colorScheme.secondary)
}

@Composable
fun SpanStyle.secondary3(): SpanStyle {
    return copy(color = MaterialTheme.colorScheme.secondary3)
}

@Composable
fun SpanStyle.error(): SpanStyle {
    return copy(color = MaterialTheme.colorScheme.error)
}
