package ru.mercury.vpclient.shared.ui.components.consultants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.basketText
import ru.mercury.vpclient.shared.domain.mapper.hasBasketBadge
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.icons.FittingShirt24
import ru.mercury.vpclient.shared.ui.icons.Phone24
import ru.mercury.vpclient.shared.ui.icons.Selection24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

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

        ConsultantCartActionButton(
            state = ConsultantCartActionButtonState(
                label = stringResource(ClientStrings.ConsultantActionCart),
                cartText = entity.basketText,
                showBadge = entity.hasBasketBadge,
                onClick = { onClick(EmployeeEntity.ID_CART) }
            ),
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

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ConsultantActionsRowPreview(
    @PreviewParameter(ConsultantActionsRowEmployeeEntityProvider::class) entity: EmployeeEntity
) {
    ConsultantActionsRow(
        entity = entity,
        onClick = {}
    )
}

private class ConsultantActionsRowEmployeeEntityProvider: PreviewParameterProvider<EmployeeEntity> {
    override val values: Sequence<EmployeeEntity> = sequenceOf(
        EmployeeEntity(
            employeeId = "1",
            employeeEmail = "anna@example.com",
            employeeMiddleName = "",
            employeeName = "Анна",
            employeePhone = "+79990000000",
            employeeSurname = "Смирнова",
            photoUrl = "https://i.pravatar.cc/144?img=32",
            previewPhotoUrl = "https://i.pravatar.cc/144?img=32",
            lastActivityColorHex = "",
            lastActivityDate = "",
            employeeBotiqueAddress = "Барвиха Luxury Village",
            employeeBotiqueAddressShort = "Барвиха Luxury Village",
            employeeBrand = "MVST",
            isActive = false,
            basketBadge = 1,
            fittingNumber = 2,
            fittingBadge = 1,
            messengerBadge = 1,
            orderBadge = 1,
            compilationBadge = 0
        ),
        EmployeeEntity.Empty
    )
}
