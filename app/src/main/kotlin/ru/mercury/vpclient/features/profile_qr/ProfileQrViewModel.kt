package ru.mercury.vpclient.features.profile_qr

import android.util.Base64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.profile_qr.intent.ProfileQrIntent
import ru.mercury.vpclient.features.profile_qr.model.ProfileQrModel
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileQrViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<ProfileQrIntent, ProfileQrModel, Event>(ProfileQrModel()) {

    init {
        dispatch(ProfileQrIntent.LoadQrCode)
    }

    override fun dispatch(intent: ProfileQrIntent) {
        when (intent) {
            is ProfileQrIntent.LoadQrCode -> {
                launch {
                    runCatching { interactor.userId() }
                        .onSuccess { userId ->
                            val timestamp = System.currentTimeMillis() / 1000
                            val qrCode = Base64.encodeToString("${timestamp}_$userId".toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
                            reduce { it.copy(qrCode = qrCode) }
                        }
                }
            }
            is ProfileQrIntent.CloseClick -> launch { MainEventManager.send(BackRoute) }
        }
    }
}
