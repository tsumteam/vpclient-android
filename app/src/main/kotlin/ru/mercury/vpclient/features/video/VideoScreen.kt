@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.video

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import ru.mercury.vpclient.features.video.intent.VideoIntent
import ru.mercury.vpclient.features.video.model.VideoModel
import ru.mercury.vpclient.features.video.navigation.VideoRoute
import ru.mercury.vpclient.shared.ui.components.NightSystemBars
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.video.VideoControls
import ru.mercury.vpclient.shared.ui.components.video.VideoControlsState
import ru.mercury.vpclient.shared.ui.components.video.VideoPlaybackButton
import ru.mercury.vpclient.shared.ui.components.video.VideoPlayer
import ru.mercury.vpclient.shared.ui.components.video.VideoSpeedDropdownMenu
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.Settings24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import kotlin.time.Duration.Companion.milliseconds

private const val VIDEO_CONTROLS_HIDE_DELAY_MILLIS = 2_000L
private const val VIDEO_POSITION_UPDATE_DELAY_MILLIS = 250L

@Composable
fun VideoScreen(
    route: VideoRoute,
    viewModel: VideoViewModel = hiltViewModel<VideoViewModel, VideoViewModel.Factory>(creationCallback = { it.create(route) })
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    NightSystemBars()

    VideoScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun VideoScreenContent(
    state: VideoModel,
    dispatch: (VideoIntent) -> Unit
) {
    var player by remember { mutableStateOf<ExoPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var positionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(0L) }
    var selectedSpeed by remember { mutableFloatStateOf(1F) }
    var speedMenuExpanded by remember { mutableStateOf(false) }
    var controlsVisible by remember { mutableStateOf(true) }
    var controlsVisibilityTick by remember { mutableIntStateOf(0) }

    LaunchedEffect(player) {
        while (player != null) {
            val currentPlayer = player ?: return@LaunchedEffect
            positionMs = currentPlayer.currentPosition.coerceAtLeast(0L)
            durationMs = currentPlayer.duration.takeIf { it > 0L } ?: 0L
            delay(VIDEO_POSITION_UPDATE_DELAY_MILLIS.milliseconds)
        }
    }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) {
            controlsVisible = true
        }
    }

    LaunchedEffect(
        controlsVisible,
        controlsVisibilityTick,
        isPlaying,
        speedMenuExpanded
    ) {
        if (controlsVisible && isPlaying && !speedMenuExpanded) {
            delay(VIDEO_CONTROLS_HIDE_DELAY_MILLIS.milliseconds)
            controlsVisible = false
        }
    }

    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    SharedAnimatedVisibility(
                        visible = controlsVisible
                    ) {
                        IconButton(
                            onClick = { dispatch(VideoIntent.BackClick) },
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                actions = {
                    SharedAnimatedVisibility(
                        visible = controlsVisible
                    ) {
                        Box {
                            IconButton(
                                onClick = {
                                    speedMenuExpanded = true
                                    controlsVisible = true
                                    controlsVisibilityTick += 1
                                }
                            ) {
                                Icon(
                                    imageVector = Settings24,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            VideoSpeedDropdownMenu(
                                expanded = speedMenuExpanded,
                                selectedSpeed = selectedSpeed,
                                onDismissRequest = { speedMenuExpanded = false },
                                onSpeedClick = { speed ->
                                    selectedSpeed = speed
                                    player?.setPlaybackSpeed(speed)
                                    speedMenuExpanded = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            SharedAnimatedVisibility(
                visible = controlsVisible,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                VideoControls(
                    state = VideoControlsState(
                        positionMs = positionMs,
                        durationMs = durationMs
                    ),
                    onSeek = { seekPositionMs ->
                        controlsVisible = true
                        controlsVisibilityTick += 1
                        player?.seekTo(seekPositionMs)
                        positionMs = seekPositionMs
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .pointerInput(controlsVisible, isPlaying) {
                    detectTapGestures {
                        when {
                            controlsVisible && isPlaying -> controlsVisible = false
                            else -> {
                                controlsVisible = true
                                controlsVisibilityTick += 1
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            VideoPlayer(
                videoUrl = state.videoUrl,
                isVisible = true,
                modifier = Modifier.fillMaxWidth(),
                keepAspectRatio = true,
                onPlayerChanged = { exoPlayer ->
                    player = exoPlayer
                    exoPlayer?.setPlaybackSpeed(selectedSpeed)
                },
                onIsPlayingChanged = { playing -> isPlaying = playing }
            )

            SharedAnimatedVisibility(
                visible = controlsVisible
            ) {
                VideoPlaybackButton(
                    isPlaying = isPlaying,
                    onClick = {
                        controlsVisible = true
                        controlsVisibilityTick += 1
                        player?.let { currentPlayer ->
                            when {
                                currentPlayer.isPlaying -> currentPlayer.pause()
                                else -> currentPlayer.play()
                            }
                        }
                    }
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun VideoScreenContentPreview() {
    VideoScreenContent(
        state = VideoModel(
            videoUrl = ""
        ),
        dispatch = {}
    )
}
