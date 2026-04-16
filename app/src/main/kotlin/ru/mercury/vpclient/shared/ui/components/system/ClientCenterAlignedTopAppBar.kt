@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.shared.ui.components.system

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.TopBarState
import ru.mercury.vpclient.shared.ui.components.filters.FilterScreenTitle
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.Logo82
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.preview.TopBarStateProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.medium17
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun ClientCenterAlignedTopAppBar(
    state: TopBarState
) {
    CenterAlignedTopAppBar(
        title = {
            when (state) {
                is TopBarState.Logo -> {
                    Icon(
                        imageVector = Logo82,
                        contentDescription = null,
                        modifier = Modifier.size(82.dp, 57.dp),
                        tint = Color.Black
                    )
                }
                is TopBarState.Title -> {
                    Text(
                        text = stringResource(ClientStrings.MainTabBrands),
                        style = MaterialTheme.typography.medium17.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                is TopBarState.Catalog -> {
                    Text(
                        text = stringResource(ClientStrings.MainTabCatalog),
                        style = MaterialTheme.typography.medium17.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                is TopBarState.Category -> {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                is TopBarState.Filter -> {
                    FilterScreenTitle(
                        entity = state.entity,
                        onClick = state.onClick,
                        modifier = Modifier.padding(end = 80.dp)
                    )
                }
                else -> {}
            }
        },
        navigationIcon = {
            when (state) {
                is TopBarState.Catalog -> {
                    IconButton(
                        onClick = state.navigationClick
                    ) {
                        Icon(
                            imageVector = Search24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                is TopBarState.Details -> {
                    IconButton(
                        onClick = state.navigationClick
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                is TopBarState.Category -> {
                    Row {
                        IconButton(
                            onClick = state.navigationClick
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(
                            onClick = state.searchClick
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                is TopBarState.Filter -> {
                    Row {
                        IconButton(
                            onClick = state.navigationClick
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(
                            onClick = state.searchClick
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                else -> {}
            }
        },
        actions = {
            /*IndicatorIconButton(
                icon = FittingShirt24,
                showIndicator = true,
                onClick = {}
            )

            IndicatorIconButton(
                icon = Basket24,
                showIndicator = true,
                onClick = {}
            )

            IndicatorIconButton(
                icon = Chat24,
                showIndicator = true,
                onClick = {},
                modifier = Modifier.padding(end = 8.dp)
            )*/ // fixme
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent
        )
    )
}

@FontScalePreviews
@Composable
private fun ClientCenterAlignedTopAppBarPreview(
    @PreviewParameter(TopBarStateProvider::class) state: TopBarState
) {
    ClientTheme {
        ClientCenterAlignedTopAppBar(state = state)
    }
}
