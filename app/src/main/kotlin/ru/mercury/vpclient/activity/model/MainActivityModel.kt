package ru.mercury.vpclient.activity.model

import androidx.navigation3.runtime.NavKey
import ru.mercury.vpclient.shared.mvi.Model

data class MainActivityModel(
    val splashLoading: Boolean = true,
    val startDestination: NavKey? = null,
    val centerLoading: Boolean = false
): Model
