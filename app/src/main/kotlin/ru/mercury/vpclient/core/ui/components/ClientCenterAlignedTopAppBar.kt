@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.core.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.mercury.vpclient.core.ui.theme.ClientIcons
import ru.mercury.vpclient.core.ui.theme.ClientTheme

@Composable
fun ClientCenterAlignedTopAppBar(
    title: @Composable () -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors().copy(containerColor = Color.Transparent)
) {
    CenterAlignedTopAppBar(
        title = title,
        colors = colors
    )
}

@Preview
@Composable
private fun ClientCenterAlignedTopAppBarPreview() {
    ClientTheme {
        Scaffold(
            topBar = {
                ClientCenterAlignedTopAppBar(
                    title = {
                        Icon(
                            painter = painterResource(ClientIcons.Logo82),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                )
            }
        ) { innerPadding ->
            innerPadding.toString()
        }
    }
}
