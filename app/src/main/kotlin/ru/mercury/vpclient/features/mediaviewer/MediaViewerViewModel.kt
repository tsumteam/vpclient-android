package ru.mercury.vpclient.features.mediaviewer

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.mediaviewer.intent.MediaViewerIntent
import ru.mercury.vpclient.features.mediaviewer.model.MediaViewerModel
import ru.mercury.vpclient.features.mediaviewer.navigation.MediaViewerRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = MediaViewerViewModel.Factory::class)
class MediaViewerViewModel @AssistedInject constructor(
    @Assisted route: MediaViewerRoute
): ClientViewModel<MediaViewerIntent, MediaViewerModel, Event>(
    MediaViewerModel(
        imageUrls = route.imageUrls,
        videoUrl = route.videoUrl,
        initialPage = route.initialPage
    )
) {

    override fun dispatch(intent: MediaViewerIntent) {
        when (intent) {
            is MediaViewerIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: MediaViewerRoute): MediaViewerViewModel
    }
}
