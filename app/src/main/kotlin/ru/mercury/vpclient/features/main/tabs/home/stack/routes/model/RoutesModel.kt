package ru.mercury.vpclient.features.main.tabs.home.stack.routes.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.core.mvi.Model

data class RoutesModel(
    val myBoutiquesSectionExpanded: Boolean? = null,
    val myDeliveriesSectionExpanded: Boolean? = null,
    val myReturnsSectionExpanded: Boolean? = null,
    val selectedPhone: String = "",
    val phoneDialog: Boolean = false,
    val routesJob: Job? = null,
    val closeRouteJob: Job? = null,
    val isRefreshing: Boolean = false
): Model
