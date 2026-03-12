package ru.mercury.vpclient.features.main.tabs.brands

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.features.main.tabs.brands.intent.BrandsIntent
import ru.mercury.vpclient.features.main.tabs.brands.model.BrandsModel
import javax.inject.Inject

@HiltViewModel
class BrandsViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<BrandsIntent, BrandsModel, Event>(BrandsModel()) {

    override fun dispatch(intent: BrandsIntent) {
        TODO("Not yet implemented")
    }
}
