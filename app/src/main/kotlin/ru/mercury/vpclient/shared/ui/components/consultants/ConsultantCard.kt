package ru.mercury.vpclient.shared.ui.components.consultants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.ConsultantAvatarPlaceholderStyle
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo
import ru.mercury.vpclient.shared.domain.mapper.employeeFullName
import ru.mercury.vpclient.shared.domain.mapper.isPhotoEmpty
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun ConsultantCard(
    employeePojo: EmployeePojo,
    onActionClick: (Int) -> Unit,
    onActiveClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (employeePojo == EmployeePojo.Empty) Modifier else Modifier.clickable(onClick = onClick))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when {
                    employeePojo == EmployeePojo.Empty -> {
                        ConsultantAvatarPlaceholder(
                            name = employeePojo.entity.employeeName,
                            style = ConsultantAvatarPlaceholderStyle.Card,
                            modifier = Modifier.placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = CircleShape
                            )
                        )
                    }
                    employeePojo.isPhotoEmpty -> {
                        ConsultantAvatarPlaceholder(
                            name = employeePojo.entity.employeeName,
                            style = ConsultantAvatarPlaceholderStyle.Card
                        )
                    }
                    else -> {
                        ClientAsyncImage(
                            imageUrl = employeePojo.entity.previewPhotoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 16.dp, end = 8.dp)
                        .placeholder(
                            visible = employeePojo == EmployeePojo.Empty,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    Text(
                        text = employeePojo.employeeFullName,
                        maxLines = 1,
                        style = MaterialTheme.typography.regular15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 19.sp,
                            letterSpacing = .2.sp
                        )
                    )
                    BrandBox(
                        entity = BrandEntity(
                            brand = employeePojo.entity.employeeBrand,
                            urlBrandLogo = null
                        )
                    )
                }

                when {
                    employeePojo.entity.isActive -> {
                        ConsultantActiveBadge(
                            modifier = Modifier.placeholder(
                                visible = employeePojo == EmployeePojo.Empty,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = RoundedCornerShape(4.dp)
                            )
                        )
                    }
                    else -> {
                        ConsultantInactiveButton(
                            onClick = onActiveClick,
                            modifier = Modifier.placeholder(
                                visible = employeePojo == EmployeePojo.Empty,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = RoundedCornerShape(4.dp)
                            )
                        )
                    }
                }
            }

            ConsultantActionsRow(
                employeePojo = employeePojo,
                onClick = onActionClick,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ConsultantCardPreview(
    @PreviewParameter(ConsultantCardEmployeeEntityProvider::class) employee: EmployeePojo
) {
    ConsultantCard(
        employeePojo = employee,
        onActionClick = {},
        onActiveClick = {},
        onClick = {}
    )
}

private class ConsultantCardEmployeeEntityProvider: PreviewParameterProvider<EmployeePojo> {
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
