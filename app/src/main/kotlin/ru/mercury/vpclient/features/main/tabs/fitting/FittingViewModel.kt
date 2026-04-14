package ru.mercury.vpclient.features.main.tabs.fitting

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mercury.vpclient.shared.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.features.main.tabs.fitting.intent.FittingIntent
import ru.mercury.vpclient.features.main.tabs.fitting.model.FittingModel
import javax.inject.Inject

@HiltViewModel
class FittingViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<FittingIntent, FittingModel, Event>(FittingModel()) {

    override fun dispatch(intent: FittingIntent) {
        TODO("Not yet implemented")
    }
}
