package ru.mercury.vpclient.features.profile_loyalty_terms

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.profile_loyalty_terms.intent.ProfileLoyaltyTermsIntent
import ru.mercury.vpclient.features.profile_loyalty_terms.model.ProfileLoyaltyTermsModel
import ru.mercury.vpclient.features.profile_loyalty_terms.navigation.ProfileLoyaltyTermsRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = ProfileLoyaltyTermsViewModel.Factory::class)
class ProfileLoyaltyTermsViewModel @AssistedInject constructor(
    @Assisted private val route: ProfileLoyaltyTermsRoute
): ClientViewModel<ProfileLoyaltyTermsIntent, ProfileLoyaltyTermsModel, Event>(ProfileLoyaltyTermsModel()) {

    init {
        dispatch(ProfileLoyaltyTermsIntent.LoadUrl)
    }

    override fun dispatch(intent: ProfileLoyaltyTermsIntent) {
        when (intent) {
            is ProfileLoyaltyTermsIntent.LoadUrl -> reduce { it.copy(url = route.url) }
            is ProfileLoyaltyTermsIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: ProfileLoyaltyTermsRoute): ProfileLoyaltyTermsViewModel
    }
}
