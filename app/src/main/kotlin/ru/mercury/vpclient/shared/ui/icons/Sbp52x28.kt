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

val Sbp52x28: ImageVector
    get() {
        if (_sbp52x28 != null) {
            return _sbp52x28!!
        }
        _sbp52x28 = ImageVector.Builder(
            name = "Sbp52x28",
            defaultWidth = 52.dp,
            defaultHeight = 28.dp,
            viewportWidth = 52F,
            viewportHeight = 28F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    "M51.9997 10.3498V18.3945H49.3227V12.7513H46.7451V18.3945H44.0681V10.3492H51.9997V10.3498Z"
                ),
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M38.5707 18.6821C40.9671 18.6821 42.7466 17.1054 42.7466 14.7151C42.7466 12.4016 41.4337 10.8995 39.2394 10.8995C38.2267 10.8995 37.3911 11.282 36.7617 11.9421C36.912 10.5779 37.987 9.58195 39.1707 9.58195C39.4438 9.58195 41.5004 9.57729 41.5004 9.57729L42.6633 7.18921C42.6633 7.18921 40.0814 7.25228 38.8812 7.25228C36.1388 7.30355 34.2864 9.97845 34.2864 13.2273C34.2864 17.0122 36.093 18.6821 38.5707 18.6821ZM38.5852 13.0148C39.475 13.0148 40.0919 13.6421 40.0919 14.7149C40.0919 15.6803 39.5438 16.4758 38.5852 16.478C37.6684 16.478 37.0515 15.7409 37.0515 14.7313C37.0515 13.6583 37.6684 13.0148 38.5852 13.0148Z"
                ),
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    "M32.1072 15.7012C32.1072 15.7012 31.475 16.0922 30.5308 16.1662C29.4453 16.2008 28.4781 15.4651 28.4781 14.1579C28.4781 12.8829 29.3315 12.1521 30.5034 12.1521C31.222 12.1521 32.1726 12.6865 32.1726 12.6865C32.1726 12.6865 32.8682 11.3168 33.2285 10.6319C32.5687 10.0953 31.6899 9.80103 30.6677 9.80103C28.0881 9.80103 26.0901 11.6059 26.0901 14.1417C26.0901 16.71 27.968 18.4729 30.6677 18.4197C31.4223 18.3895 32.4634 18.1051 33.0979 17.6678L32.1072 15.7012Z"
                ),
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M0 6.09473L3.15989 12.1556V15.8525L0.00369654 21.9014L0 6.09473Z"),
                fill = SolidColor(Color(0xFF5B57A2)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M12.1326 9.95005L15.0935 8.00265L21.1532 7.99658L12.1326 13.9265V9.95005Z"),
                fill = SolidColor(Color(0xFFD90751)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M12.1161 6.05896L12.1329 14.0833L8.96558 11.995V0L12.1163 6.05896H12.1161Z"),
                fill = SolidColor(Color(0xFFFAB718)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M21.1535 7.99656L15.0936 8.00263L12.1161 6.05896L8.96558 0L21.1533 7.99656H21.1535Z"),
                fill = SolidColor(Color(0xFFED6F26)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M12.1329 21.935V18.0418L8.96558 15.9932L8.96732 28L12.1329 21.935Z"),
                fill = SolidColor(Color(0xFF63B22F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M15.0862 20.0053L3.15967 12.1556L0 6.09473L21.1405 19.9974L15.086 20.0053H15.0862Z"),
                fill = SolidColor(Color(0xFF1487C9)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M8.96777 27.9999L12.1329 21.9349L15.0864 20.0052L21.1407 19.9973L8.96777 27.9999Z"),
                fill = SolidColor(Color(0xFF017F36)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M0.00317383 21.9014L8.99077 15.9934L5.96917 14.004L3.15937 15.8525L0.00317383 21.9014Z"),
                fill = SolidColor(Color(0xFF984995)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _sbp52x28!!
    }

private var _sbp52x28: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Sbp52x28Preview() {
    Icon(
        imageVector = Sbp52x28,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
