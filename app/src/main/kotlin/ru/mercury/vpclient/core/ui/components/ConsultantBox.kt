package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.mercury.vpclient.core.entity.ConsultantActionModel
import ru.mercury.vpclient.core.ui.icons.Brands24
import ru.mercury.vpclient.core.ui.icons.Catalog24
import ru.mercury.vpclient.core.ui.icons.Consultants24
import ru.mercury.vpclient.core.ui.icons.Fitting24
import ru.mercury.vpclient.core.ui.icons.Home24
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium16
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.core.ui.theme.secondary5

@Composable
fun ConsultantBox(
    name: String,
    avatarUrl: String,
    actions: List<ConsultantActionModel>,
    isActive: Boolean,
    onActionClick: (ConsultantActionModel) -> Unit,
    onActiveClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = name.replaceFirst(" ", "\n")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = displayName,
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 16.dp),
                    maxLines = 2,
                    style = MaterialTheme.typography.medium16.onBackground()
                )

                ConsultantActiveButton(
                    isActive = isActive,
                    onClick = onActiveClick
                )
            }

            ConsultantActionsRow(
                actions = actions,
                onActionClick = onActionClick,
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

@Preview
@Composable
private fun ConsultantBoxPreview() {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ConsultantBox(
                name = "Анна Смирнова",
                avatarUrl = "https://i.pravatar.cc/144?img=32",
                actions = listOf(
                    ConsultantActionModel(id = "call", label = "Позвонить", icon = Home24),
                    ConsultantActionModel(id = "fitting", label = "Примерка", icon = Fitting24),
                    ConsultantActionModel(id = "cart", label = "Корзина", icon = Catalog24),
                    ConsultantActionModel(id = "chat", label = "Чат", icon = Consultants24, showNotificationBadge = true),
                    ConsultantActionModel(id = "selection", label = "Подборка", icon = Brands24)
                ),
                isActive = false,
                onActionClick = {},
                onActiveClick = {},
                onClick = {}
            )
        }
    }
}
