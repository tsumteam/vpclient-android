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
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo
import ru.mercury.vpclient.shared.domain.mapper.basketText
import ru.mercury.vpclient.shared.domain.mapper.compilationBadge
import ru.mercury.vpclient.shared.domain.mapper.fittingText
import ru.mercury.vpclient.shared.domain.mapper.hasBasketBadge
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.messengerBadge
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.icons.Selection24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

@Composable
fun ConsultantActionsRow(
    employeePojo: EmployeePojo,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        ConsultantPhoneActionButton(
            label = stringResource(ClientStrings.ConsultantActionCall),
            onClick = { onClick(EmployeeEntity.ID_CALL) },
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = employeePojo == EmployeePojo.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        ConsultantFittingActionButton(
            state = ConsultantFittingActionButtonState(
                label = stringResource(ClientStrings.ConsultantActionFitting),
                fittingText = employeePojo.fittingText,
                showBadge = employeePojo.hasFittingBadge,
                onClick = { onClick(EmployeeEntity.ID_FITTING) }
            ),
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = employeePojo == EmployeePojo.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        ConsultantCartActionButton(
            state = ConsultantCartActionButtonState(
                label = stringResource(ClientStrings.ConsultantActionCart),
                cartText = employeePojo.basketText,
                showBadge = employeePojo.hasBasketBadge,
                onClick = { onClick(EmployeeEntity.ID_CART) }
            ),
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = employeePojo == EmployeePojo.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        ConsultantActionButton(
            icon = Chat24,
            label = stringResource(ClientStrings.ConsultantActionChat),
            badge = employeePojo.messengerBadge > 0,
            onClick = { onClick(EmployeeEntity.ID_CHAT) },
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = employeePojo == EmployeePojo.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        ConsultantActionButton(
            icon = Selection24,
            label = stringResource(ClientStrings.ConsultantActionSelection),
            badge = employeePojo.compilationBadge > 0,
            onClick = { onClick(EmployeeEntity.ID_SELECTION) },
            modifier = Modifier
                .weight(1F)
                .placeholder(
                    visible = employeePojo == EmployeePojo.Empty,
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
    @PreviewParameter(ConsultantActionsRowEmployeeEntityProvider::class) employee: EmployeePojo
) {
    ConsultantActionsRow(
        employeePojo = employee,
        onClick = {}
    )
}

private class ConsultantActionsRowEmployeeEntityProvider: PreviewParameterProvider<EmployeePojo> {
    override val values: Sequence<EmployeePojo> = sequenceOf(
        EmployeePojo(
            entity = EmployeeEntity(
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
                position = 0,
                basketNumber = 0,
                basketBadge = 0,
                fittingNumber = 0,
                fittingBadge = 0,
                messengerBadge = 0,
                orderBadge = 0,
                compilationBadge = 0
            ),
            badgeEntity = EmployeeBadgeEntity.Empty.copy(employeeId = "1")
        ),
        EmployeePojo.Empty
    )
}
