package ru.mercury.vpclient.features.main.tabs.home

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mercury.vpclient.shared.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.features.main.tabs.home.intent.HomeIntent
import ru.mercury.vpclient.features.main.tabs.home.model.HomeModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<HomeIntent, HomeModel, Event>(HomeModel()) {

    override fun dispatch(intent: HomeIntent) {
        TODO("Not yet implemented")
    }
}
