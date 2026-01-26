package ru.mercury.vpclient.features.main

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mercury.vpclient.core.mvi.VPClientViewModel
import ru.mercury.vpclient.features.main.intent.MainIntent
import ru.mercury.vpclient.features.main.model.MainModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): VPClientViewModel<MainIntent, MainModel>(MainModel()) {

    override fun dispatch(intent: MainIntent) {
        when (intent) {
            is MainIntent.SelectTab -> {
                reduce { it.copy(selectedRoute = intent.route) }
            }
        }
    }
}
