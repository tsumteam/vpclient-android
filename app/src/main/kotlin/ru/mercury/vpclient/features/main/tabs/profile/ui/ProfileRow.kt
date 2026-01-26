package ru.mercury.vpclient.features.main.tabs.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientStrings
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography

@Composable
fun ProfileRow(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    ) {
        val (startIcon, titleText, endIcon) = createRefs()

        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.constrainAs(startIcon) {
                width = Dimension.value(24.dp)
                height = Dimension.value(24.dp)
                start.linkTo(parent.start, 16.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = text,
            modifier = Modifier.constrainAs(titleText) {
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
                start.linkTo(startIcon.end, 16.dp)
                top.linkTo(parent.top)
                end.linkTo(endIcon.start, 16.dp)
                bottom.linkTo(parent.bottom)
            },
            style = VPClientTypography.Regular_14_OnBackground
        )

        Icon(
            painter = painterResource(VPClientIcons.ArrawEnd),
            contentDescription = null,
            modifier = Modifier.constrainAs(endIcon) {
                width = Dimension.value(24.dp)
                height = Dimension.value(24.dp)
                top.linkTo(parent.top)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom)
            },
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Preview(fontScale = 1.5F)
@Composable
private fun ProfileRowPreview() {
    VPClientTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            ProfileRow(
                painter = painterResource(VPClientIcons.Reciept),
                text = stringResource(VPClientStrings.AppName)
            )
        }
    }
}
