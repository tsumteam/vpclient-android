package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.CatalogTabData
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.CatalogTabsProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretRegular14
import ru.mercury.vpclient.shared.ui.theme.surface4

@Composable
fun CatalogTabRow(
    tabs: List<CatalogTabData>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        tabs.isEmpty() -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(44.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .height(32.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = MaterialTheme.colorScheme.surface4,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
        else -> {
            SecondaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(44.dp),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                divider = {},
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(
                                selectedTabIndex = selectedTabIndex,
                                matchContentSize = false
                            ),
                        height = 4.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { onTabClick(index) },
                        selectedContentColor = MaterialTheme.colorScheme.onBackground,
                        unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                        text = {
                            Text(
                                text = tab.title.uppercase(),
                                style = MaterialTheme.typography.livretRegular14
                            )
                        }
                    )
                }
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun CatalogTabRowPreview(
    @PreviewParameter(CatalogTabsProvider::class) tabs: List<CatalogTabData>
) {
    ClientTheme {
        CatalogTabRow(
            tabs = tabs,
            selectedTabIndex = 0,
            onTabClick = {}
        )
    }
}
