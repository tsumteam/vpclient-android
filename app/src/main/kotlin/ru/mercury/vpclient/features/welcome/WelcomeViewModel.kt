package ru.mercury.vpclient.features.welcome

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.EmptyModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.features.login.navigation.LoginRoute
import ru.mercury.vpclient.features.register.navigation.RegisterRoute
import ru.mercury.vpclient.features.welcome.intent.WelcomeIntent
import ru.mercury.vpclient.activity.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(): ClientViewModel<WelcomeIntent, Model, Event>(EmptyModel) {

    override fun dispatch(intent: WelcomeIntent) {
        when (intent) {
            is WelcomeIntent.RegisterClick -> launch { MainEventManager.send(RegisterRoute) }
            is WelcomeIntent.LoginClick -> launch { MainEventManager.send(LoginRoute) }
        }
    }
}
