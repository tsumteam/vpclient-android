@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.icons.Logo82
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
                            imageVector = Logo82,
                            contentDescription = null,
                            modifier = Modifier.size(82.dp, 57.dp),
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
