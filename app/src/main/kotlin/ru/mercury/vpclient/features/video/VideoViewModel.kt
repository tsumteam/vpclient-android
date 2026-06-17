package ru.mercury.vpclient.features.video

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.video.intent.VideoIntent
import ru.mercury.vpclient.features.video.model.VideoModel
import ru.mercury.vpclient.features.video.navigation.VideoRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = VideoViewModel.Factory::class)
class VideoViewModel @AssistedInject constructor(
    @Assisted private val route: VideoRoute
): ClientViewModel<VideoIntent, VideoModel, Event>(VideoModel()) {

    init {
        dispatch(VideoIntent.CollectVideo)
    }

    override fun dispatch(intent: VideoIntent) {
        when (intent) {
            is VideoIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is VideoIntent.CollectVideo -> reduce { it.copy(videoUrl = route.videoUrl) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: VideoRoute): VideoViewModel
    }
}
