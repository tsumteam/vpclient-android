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

val GiftCard24: ImageVector
    get() {
        if (_giftCard24 != null) {
            return _giftCard24!!
        }
        _giftCard24 = ImageVector.Builder(
            name = "GiftCard24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M18.5158 6.77957H15.4619C15.9477 6.5053 16.3503 6.21385 16.5655 5.99862C17.4782 5.08599
                    17.4782 3.59704 16.5655 2.68447C15.6529 1.77184 14.1639 1.77184 13.2514 2.68447C12.9633
                    2.97253 12.5329 3.60411 12.1998 4.31552C11.8667 3.60411 11.4363 2.97251 11.1482 2.68447C10.2356
                    1.77184 8.74666 1.77184 7.83409 2.68447C6.92146 3.5971 6.92146 5.08606 7.83409 5.99862C8.0839
                    6.24844 8.4831 6.52948 8.9273 6.77957H5.88377C4.29437 6.77957 3 8.07394 3 9.66334V19.1162C3
                    20.7056 4.29437 22 5.88377 22H18.5089C20.0983 22 21.3927 20.7056 21.3927 19.1162L21.3932
                    9.66334C21.3999 8.07053 20.1056 6.77957 18.5161 6.77957H18.5158ZM20.3587 9.66334V11.9885C20.3413
                    11.9885 20.3241 11.9851 20.3033 11.9851H12.7237V7.82075H18.5157C19.5327 7.8205 20.3586
                    8.64643 20.3586 9.66336L20.3587 9.66334ZM13.9907 3.41987C14.2441 3.16643 14.5772 3.03812
                    14.9103 3.03812C15.2434 3.03812 15.5767 3.16642 15.8299 3.41987C16.3366 3.92652 16.3366
                    4.75247 15.8299 5.26248C15.0839 6.00854 13.1302 6.77201 12.8072 6.44599C12.4812 6.11629
                    13.2447 4.16617 13.9907 3.41987H13.9907ZM8.1919 4.3395C8.1919 3.99237 8.32722 3.66632
                    8.57365 3.41987C8.82008 3.17344 9.14614 3.03812 9.49328 3.03812C9.84042 3.03812 10.1665
                    3.17344 10.4129 3.41987C10.6872 3.69753 11.1347 4.38449 11.4192 5.12039C11.7247 5.90809
                    11.6898 6.34888 11.5927 6.44258C11.4991 6.53626 11.0549 6.5745 10.2706 6.26901C9.53491
                    5.98433 8.84426 5.53678 8.57004 5.26274C8.32724 5.01631 8.19193 4.68662 8.19193 4.33952L8.1919
                    4.3395ZM5.89102 7.82023H11.683V11.9845L4.10412 11.9848C4.08669 11.9848 4.06587 11.9882 4.04868
                    11.9882V9.66307C4.04844 8.64614 4.87437 7.8202 5.8913 7.8202L5.89102 7.82023ZM4.04816
                    19.1161V13.0188C4.06559 13.0188 4.08278 13.0222 4.10359 13.0222H11.6831V20.9556H5.89112C4.87419
                    20.9592 4.04825 20.1333 4.04825 19.1163L4.04816 19.1161ZM18.5157 20.959H12.7236V13.0256H20.3026C20.32
                    13.0256 20.3408 13.0222 20.358 13.0222V19.1195C20.3582 20.133 19.5323 20.959 18.5154 20.959L18.5157 20.959Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _giftCard24!!
    }

private var _giftCard24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun GiftCard24Preview() {
    Icon(
        imageVector = GiftCard24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
