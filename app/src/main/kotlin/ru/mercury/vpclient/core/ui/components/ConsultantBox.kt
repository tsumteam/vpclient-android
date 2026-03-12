package ru.mercury.vpclient.core.ui.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.core.ui.PlaceholderHighlight
import ru.mercury.vpclient.core.ui.fade
import ru.mercury.vpclient.core.ui.placeholder
import ru.mercury.vpclient.core.ui.preview.EmployeeEntityProvider
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium16
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.core.ui.theme.secondary5

@Composable
fun ConsultantBox(
    employee: EmployeeEntity,
    onActionClick: (Int) -> Unit,
    onActiveClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = if (employee == EmployeeEntity.Empty) "Имя\nФамилия" else "${employee.employeeName}\n${employee.employeeSurname}"

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
                ClientAsyncImage(
                    imageUrl = employee.photoUrl.ifEmpty { employee.previewPhotoUrl },
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .placeholder(
                            visible = employee == EmployeeEntity.Empty,
                            highlight = PlaceholderHighlight.fade(),
                            shape = CircleShape
                        )
                )

                Text(
                    text = displayName,
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 16.dp, end = 8.dp)
                        .placeholder(
                            visible = employee == EmployeeEntity.Empty,
                            highlight = PlaceholderHighlight.fade(),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    maxLines = 2,
                    style = MaterialTheme.typography.medium16.onBackground()
                )

                when {
                    employee.isActive -> {
                        ConsultantActiveBadge(
                            modifier = Modifier.placeholder(
                                visible = employee == EmployeeEntity.Empty,
                                highlight = PlaceholderHighlight.fade(),
                                shape = RoundedCornerShape(4.dp)
                            )
                        )
                    }
                    else -> {
                        ConsultantMakeActiveButton(
                            onClick = onActiveClick,
                            modifier = Modifier.placeholder(
                                visible = employee == EmployeeEntity.Empty,
                                highlight = PlaceholderHighlight.fade(),
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

@Preview(showBackground = true)
@Composable
private fun ConsultantBoxPreview(
    @PreviewParameter(EmployeeEntityProvider::class) entity: EmployeeEntity
) {
    ClientTheme {
        ConsultantBox(
            employee = entity,
            onActionClick = {},
            onActiveClick = {},
            onClick = {}
        )
    }
}
