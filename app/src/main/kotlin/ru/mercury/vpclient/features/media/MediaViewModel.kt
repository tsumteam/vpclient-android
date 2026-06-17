package ru.mercury.vpclient.features.media

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.media.intent.MediaIntent
import ru.mercury.vpclient.features.media.model.MediaModel
import ru.mercury.vpclient.features.media.navigation.MediaRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = MediaViewModel.Factory::class)
class MediaViewModel @AssistedInject constructor(
    @Assisted private val route: MediaRoute
): ClientViewModel<MediaIntent, MediaModel, Event>(MediaModel()) {

    init {
        dispatch(MediaIntent.CollectMedia)
    }

    override fun dispatch(intent: MediaIntent) {
        when (intent) {
            is MediaIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is MediaIntent.CollectMedia -> {
                reduce {
                    it.copy(
                        imageUrls = route.imageUrls,
                        videoUrl = route.videoUrl,
                        initialPage = route.initialPage
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: MediaRoute): MediaViewModel
    }
}
