package ru.mercury.vpclient.features.main.tabs.home

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mercury.vpclient.core.mvi.VPClientViewModel
import ru.mercury.vpclient.features.main.tabs.home.intent.HomeIntent
import ru.mercury.vpclient.features.main.tabs.home.model.HomeModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): VPClientViewModel<HomeIntent, HomeModel>(HomeModel()) {

    override fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.NavigateRoute -> reduce { it.copy(selectedRoute = intent.route) }
        }
    }
}
