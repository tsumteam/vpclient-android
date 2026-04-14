package ru.mercury.vpclient.activity

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.features.welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.activity.intent.MainActivityIntent
import ru.mercury.vpclient.activity.model.MainActivityModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
): ClientViewModel<MainActivityIntent, MainActivityModel, Event>(MainActivityModel()) {

    init {
        dispatch(MainActivityIntent.ResolveNavigation)
    }

    override fun dispatch(intent: MainActivityIntent) {
        when (intent) {
            is MainActivityIntent.ResolveNavigation -> {
                launch {
                    val startDestination = if (settingsDataStore.getValue(PreferenceKey.UserToken).isNullOrEmpty()) WelcomeRoute else MainRoute()
                    reduce { it.copy(splashLoading = false, startDestination = startDestination) }
                }
            }
            is MainActivityIntent.CenterLoading -> reduce { it.copy(centerLoading = intent.enabled) }
        }
    }
}
