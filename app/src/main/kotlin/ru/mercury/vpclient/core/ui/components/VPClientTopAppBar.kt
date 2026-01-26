@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.mercury.vpclient.core.ui.theme.VPClientTheme

@Composable
fun VPClientTopAppBar(
    text: String,
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = text
            )
        },
        navigationIcon = {
            BackIconButton(
                onClick = onNavigationIconClick
            )
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun VPClientTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent
        )
    )
}

@Preview
@Composable
private fun VPClientTopAppBarPreview() {
    VPClientTheme {
        Scaffold(
            topBar = {
                VPClientTopAppBar(
                    text = "Товары",
                    onNavigationIconClick = {}
                )
            }
        ) { innerPadding ->
            innerPadding.toString()
        }
    }
}
