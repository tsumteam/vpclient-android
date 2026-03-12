package ru.mercury.vpclient.features.main.tabs.profile.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.core.mvi.Model

data class ProfileModel(
    val logoutJob: Job? = null
): Model {

    val isLogoutLoading: Boolean
        get() = logoutJob?.isActive == true
}
