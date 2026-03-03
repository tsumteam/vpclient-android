package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.entity.ConsultantActionModel
import ru.mercury.vpclient.core.ui.icons.Brands24
import ru.mercury.vpclient.core.ui.icons.Catalog24
import ru.mercury.vpclient.core.ui.icons.Consultants24
import ru.mercury.vpclient.core.ui.icons.Fitting24
import ru.mercury.vpclient.core.ui.icons.Home24
import ru.mercury.vpclient.core.ui.theme.ClientTheme

@Composable
fun ConsultantActionsRow(
    actions: List<ConsultantActionModel>,
    onActionClick: (ConsultantActionModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        actions.forEach { action ->
            ConsultantActionButton(
                icon = action.icon,
                text = action.label,
                showNotificationBadge = action.showNotificationBadge,
                onClick = { onActionClick(action) },
                modifier = Modifier.weight(1F)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConsultantActionsRowPreview() {
    ClientTheme {
        ConsultantActionsRow(
            actions = listOf(
                ConsultantActionModel(id = "call", label = "Позвонить", icon = Home24),
                ConsultantActionModel(id = "fitting", label = "Примерка", icon = Fitting24),
                ConsultantActionModel(id = "cart", label = "Корзина", icon = Catalog24),
                ConsultantActionModel(id = "chat", label = "Чат", icon = Consultants24, showNotificationBadge = true),
                ConsultantActionModel(id = "selection", label = "Подборка", icon = Brands24)
            ),
            onActionClick = {}
        )
    }
}
