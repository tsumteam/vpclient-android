package ru.mercury.vpclient.core.ui.components.consultants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.core.ui.PlaceholderHighlight
import ru.mercury.vpclient.core.ui.icons.Basket24
import ru.mercury.vpclient.core.ui.icons.Chat24
import ru.mercury.vpclient.core.ui.icons.FittingShirt24
import ru.mercury.vpclient.core.ui.icons.Phone24
import ru.mercury.vpclient.core.ui.icons.Selection24
import ru.mercury.vpclient.core.ui.placeholder
import ru.mercury.vpclient.core.ui.preview.EmployeeEntityProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.shimmer
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme

@Composable
fun ConsultantActionsRow(
    entity: EmployeeEntity,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        ConsultantActionButton(
            icon = Phone24,
            label = stringResource(ClientStrings.ConsultantActionCall),
            badge = entity.orderBadge > 0,
            onClick = { onClick(EmployeeEntity.ID_CALL) },
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = entity == EmployeeEntity.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        ConsultantActionButton(
            icon = FittingShirt24,
            label = stringResource(ClientStrings.ConsultantActionFitting),
            badge = entity.fittingBadge > 0,
            onClick = { onClick(EmployeeEntity.ID_FITTING) },
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = entity == EmployeeEntity.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        ConsultantActionButton(
            icon = Basket24,
            label = stringResource(ClientStrings.ConsultantActionCart),
            badge = entity.basketBadge > 0,
            onClick = { onClick(EmployeeEntity.ID_CART) },
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = entity == EmployeeEntity.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        ConsultantActionButton(
            icon = Chat24,
            label = stringResource(ClientStrings.ConsultantActionChat),
            badge = entity.messengerBadge > 0,
            onClick = { onClick(EmployeeEntity.ID_CHAT) },
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = entity == EmployeeEntity.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        ConsultantActionButton(
            icon = Selection24,
            label = stringResource(ClientStrings.ConsultantActionSelection),
            badge = entity.compilationBadge > 0,
            onClick = { onClick(EmployeeEntity.ID_SELECTION) },
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = entity == EmployeeEntity.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}

@FontScalePreviews
@Composable
private fun ConsultantActionsRowPreview(
    @PreviewParameter(EmployeeEntityProvider::class) entity: EmployeeEntity
) {
    ClientTheme {
        ConsultantActionsRow(
            entity = entity,
            onClick = {}
        )
    }
}
