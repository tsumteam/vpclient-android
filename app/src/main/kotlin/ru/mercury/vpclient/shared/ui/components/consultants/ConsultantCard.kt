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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import ru.mercury.vpclient.shared.data.entity.ConsultantAvatarPlaceholderStyle
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.EmployeeEntityProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.regular15
import ru.mercury.vpclient.shared.ui.theme.secondary5

@Composable
fun ConsultantCard(
    employee: EmployeeEntity,
    onActionClick: (Int) -> Unit,
    onActiveClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (employee == EmployeeEntity.Empty) Modifier else Modifier.clickable(onClick = onClick))
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
                SubcomposeAsyncImage(
                    model = employee.photoUrl.ifEmpty { employee.previewPhotoUrl },
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .placeholder(
                            visible = employee == EmployeeEntity.Empty,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop,
                    error = {
                        ConsultantAvatarPlaceholder(
                            name = employee.employeeName,
                            style = ConsultantAvatarPlaceholderStyle.Card
                        )
                    }
                )

                Column(
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 16.dp, end = 8.dp)
                        .placeholder(
                            visible = employee == EmployeeEntity.Empty,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    Text(
                        text = "${employee.employeeName} ${employee.employeeSurname}".trim(),
                        maxLines = 1,
                        style = MaterialTheme.typography.regular15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 19.sp,
                            letterSpacing = .2.sp
                        )
                    )
                    BrandBox(
                        brand = employee.employeeBrand,
                        urlBrandLogo = null
                    )
                }

                when {
                    employee.isActive -> {
                        ConsultantActiveBadge(
                            modifier = Modifier.placeholder(
                                visible = employee == EmployeeEntity.Empty,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = RoundedCornerShape(4.dp)
                            )
                        )
                    }
                    else -> {
                        ConsultantInactiveButton(
                            onClick = onActiveClick,
                            modifier = Modifier.placeholder(
                                visible = employee == EmployeeEntity.Empty,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = RoundedCornerShape(4.dp)
                            )
                        )
                    }
                }
            }

            ConsultantActionsRow(
                entity = employee,
                onClick = onActionClick,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.secondary5
        )
    }
}

@FontScalePreviews
@Composable
private fun ConsultantCardPreview(
    @PreviewParameter(EmployeeEntityProvider::class) entity: EmployeeEntity
) {
    ClientTheme {
        ConsultantCard(
            employee = entity,
            onActionClick = {},
            onActiveClick = {},
            onClick = {}
        )
    }
}
