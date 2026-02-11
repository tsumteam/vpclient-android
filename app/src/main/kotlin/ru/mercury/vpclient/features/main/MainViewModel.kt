package ru.mercury.vpclient.features.main

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.features.main.intent.MainIntent
import ru.mercury.vpclient.features.main.model.MainModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ClientViewModel<MainIntent, MainModel, Event>(MainModel()) {

    override fun dispatch(intent: MainIntent) {
        when (intent) {
            is MainIntent.SelectTab -> reduce { it.copy(selectedRoute = intent.route) }
        }
    }
}
