@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.mediaviewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.mediaviewer.intent.MediaViewerIntent
import ru.mercury.vpclient.features.mediaviewer.model.MediaViewerModel
import ru.mercury.vpclient.features.mediaviewer.navigation.MediaViewerRoute
import ru.mercury.vpclient.shared.data.entity.DetailsMediaItem
import ru.mercury.vpclient.shared.ui.components.details.DetailsPagerIndicator
import ru.mercury.vpclient.shared.ui.components.details.DetailsVideoPlayer
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.rememberZoomState
import ru.mercury.vpclient.shared.ui.zoomable

@Composable
fun MediaViewerScreen(
    route: MediaViewerRoute,
    viewModel: MediaViewerViewModel = hiltViewModel<MediaViewerViewModel, MediaViewerViewModel.Factory>(creationCallback = { it.create(route) })
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    MediaViewerScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun MediaViewerScreenContent(
    state: MediaViewerModel,
    dispatch: (MediaViewerIntent) -> Unit
) {
    val mediaItems = state.mediaItems
    val pagerState = rememberPagerState(
        initialPage = state.initialPage,
        pageCount = { mediaItems.size }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(MediaViewerIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            DetailsPagerIndicator(
                pagerState = pagerState,
                pageCount = mediaItems.size,
                showVideoIcon = state.hasVideo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
            userScrollEnabled = mediaItems.size > 1
        ) { page ->
            when (val item = mediaItems[page]) {
                is DetailsMediaItem.Image -> {
                    val zoomState = rememberZoomState()
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ClientAsyncImage(
                            imageUrl = item.url,
                            modifier = Modifier
                                .fillMaxSize()
                                .zoomable(zoomState),
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.TopCenter
                        )
                    }
                }
                is DetailsMediaItem.Video -> {
                    val zoomState = rememberZoomState()
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        DetailsVideoPlayer(
                            videoUrl = item.url,
                            isVisible = pagerState.currentPage == page,
                            modifier = Modifier
                                .fillMaxWidth()
                                .zoomable(zoomState),
                            keepAspectRatio = true
                        )
                    }
                }
            }
        }
    }
}
