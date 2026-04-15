package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogClothingCard
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn

@Composable
fun CatalogClothingContent(
    entities: List<CatalogCategoryEntity>,
    onItemClick: (CatalogCategoryEntity) -> Unit,
    contentPadding: PaddingValues = PaddingValues(top = 16.dp, bottom = 8.dp)
) {
    ClientLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        itemsIndexed(
            items = entities,
            key = { _, entity -> entity.id }
        ) { index, entity ->
            CatalogClothingCard(
                entity = entity,
                modifier = Modifier.clickable { onItemClick(entity) }
            )

            if (index != entities.lastIndex) {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
        }
    }
}
