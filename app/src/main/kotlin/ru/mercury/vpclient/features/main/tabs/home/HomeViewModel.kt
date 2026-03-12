package ru.mercury.vpclient.features.main.tabs.home

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
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
