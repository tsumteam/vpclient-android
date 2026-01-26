package ru.mercury.vpclient.features.main.tabs.home.stack.routes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.persistence.database.pojo.ReturnPojo
import ru.mercury.vpclient.core.ui.preview.ReturnPojoProvider
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography

@Composable
fun ReturnCard(
    pojo: ReturnPojo,
    onClick: () -> Unit,
    onDeliveryClick: (DeliveryId) -> Unit,
    onPhoneClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
    ) {
        val (boutiqueNameText, chevronButton, deliveriesColumn) = createRefs()

        Text(
            text = pojo.boutiqueEntity.boutiqueName,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = VPClientTypography.Medium_16_OnBackground,
            modifier = Modifier.constrainAs(boutiqueNameText) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                start.linkTo(parent.start, 16.dp)
                top.linkTo(parent.top, 20.dp)
                end.linkTo(chevronButton.start, 16.dp)
                bottom.linkTo(chevronButton.bottom)
            }
        )

        ChevronButton(
            onClick = onClick,
            modifier = Modifier.constrainAs(chevronButton) {
                width = Dimension.value(36.dp)
                height = Dimension.value(36.dp)
                top.linkTo(parent.top, 18.dp)
                end.linkTo(parent.end, 16.dp)
            }
        )

        Column(
            modifier = Modifier.constrainAs(deliveriesColumn) {
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
                start.linkTo(parent.start, 16.dp)
                top.linkTo(boutiqueNameText.bottom, 6.dp)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom, 18.dp)
            }
        ) {}
    }
}

@Preview
@Preview(fontScale = 1.5F)
@Composable
private fun ReturnCardPreview(
    @PreviewParameter(ReturnPojoProvider::class) pojo: ReturnPojo
) {
    VPClientTheme {
        ReturnCard(
            pojo = pojo,
            onClick = {},
            onDeliveryClick = {},
            onPhoneClick = {}
        )
    }
}
