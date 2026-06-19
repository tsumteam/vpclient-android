package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val CardDiscount: ImageVector
    get() {
        if (_cardDiscount != null) {
            return _cardDiscount!!
        }
        _cardDiscount = ImageVector.Builder(
            name = "CardDiscount",
            defaultWidth = 32.dp,
            defaultHeight = 20.dp,
            viewportWidth = 32F,
            viewportHeight = 20F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M0.840922,12.5273L6.08193,18.3227C7.0751,19.421 8.40386,20.0165 9.8238,19.9997L28.4031,19.7785C29.3219,19.7676 30.1894,19.362 30.8487,18.6329C31.5047,17.9075 31.8715,16.9482 31.8814,15.9323L31.9998,3.76455C32.0097,2.74861 31.6615,1.79783 31.0162,1.08426C30.3742,0.374337 29.5144,-0.0107069 28.5956,0.000226604L10.0163,0.221329C8.59635,0.238227 7.25565,0.865558 6.2409,1.98766L0.885866,7.90922C-0.277483,9.19565 -0.297653,11.2682 0.840922,12.5273ZM7.06715,2.90132C7.85653,2.02843 8.89936,1.5404 10.0019,1.52728L28.5829,1.3062C29.784,1.29191 30.8301,2.4394 30.8179,3.77771L30.6995,15.9473C30.6938,16.6132 30.453,17.2431 30.0191,17.7229C29.5885,18.1991 29.0189,18.4654 28.4158,18.4725L9.83651,18.6936C8.73148,18.7058 7.69872,18.2435 6.92615,17.3892L1.68519,11.5938C1.00186,10.8381 1.01396,9.59499 1.71215,8.82293L7.06715,2.90132Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFD76B6B)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M7.06715,2.90132C7.85653,2.02843 8.89936,1.5404 10.0019,1.52728L28.5829,1.3062C29.784,1.29191 30.8301,2.4394 30.8179,3.77771L30.6995,15.9473C30.6938,16.6132 30.453,17.2431 30.0191,17.7229C29.5885,18.1991 29.0189,18.4654 28.4158,18.4725L9.83651,18.6936C8.73148,18.7058 7.69872,18.2435 6.92615,17.3892L1.68519,11.5938C1.00186,10.8381 1.01396,9.59499 1.71215,8.82293L7.06715,2.90132Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFD76B6B)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M14.4734,10.3649C13.1912,10.3649 12.4376,9.39395 12.4713,7.83279L12.4714,7.82644C12.505,6.26528 13.3003,5.29431 14.5825,5.29431C15.8584,5.29431 16.6183,6.26528 16.5847,7.82644L16.5845,7.83279C16.5509,9.39395 15.7493,10.3649 14.4734,10.3649ZM15.5253,14.5788H14.2176L20.6037,5.42124H21.9113L15.5253,14.5788ZM14.4932,9.44472C15.0708,9.44472 15.42,8.85452 15.442,7.83279L15.4421,7.82644C15.4642,6.79836 15.1404,6.21451 14.5627,6.21451C13.9787,6.21451 13.6424,6.79836 13.6203,7.82644L13.6202,7.83279C13.5982,8.85452 13.9092,9.44472 14.4932,9.44472ZM21.5464,14.7057C20.2642,14.7057 19.5045,13.7284 19.5381,12.1672L19.5382,12.1609C19.5718,10.5997 20.3734,9.6351 21.6556,9.6351C22.9315,9.6351 23.6915,10.5997 23.6579,12.1609L23.6577,12.1672C23.6241,13.7284 22.8223,14.7057 21.5464,14.7057ZM21.5663,13.7855C22.1439,13.7855 22.493,13.1953 22.5152,12.1672L22.5153,12.1609C22.5373,11.1391 22.2134,10.5553 21.6358,10.5553C21.0518,10.5553 20.7092,11.1391 20.6872,12.1609L20.687,12.1672C20.6649,13.1953 20.9823,13.7855 21.5663,13.7855Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _cardDiscount!!
    }

private var _cardDiscount: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CardDiscountPreview() {
    Icon(
        imageVector = CardDiscount,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
