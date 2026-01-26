package ru.mercury.vpclient.features.main.tabs.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientStrings
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography

@Composable
fun LogoutRow(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        val (startIcon, titleText) = createRefs()

        Icon(
            painter = painterResource(VPClientIcons.Logout),
            contentDescription = null,
            modifier = Modifier.constrainAs(startIcon) {
                width = Dimension.value(24.dp)
                height = Dimension.value(24.dp)
                start.linkTo(parent.start, 16.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            text = stringResource(VPClientStrings.AppName),
            modifier = Modifier.constrainAs(titleText) {
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
                start.linkTo(startIcon.end, 16.dp)
                top.linkTo(parent.top)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom)
            },
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = VPClientTypography.Regular_14_Error
        )
    }
}

@Preview
@Preview(fontScale = 1.5F)
@Composable
private fun LogoutRowPreview() {
    VPClientTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            LogoutRow()
        }
    }
}
