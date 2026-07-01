package ru.mercury.vpclient.features.profile_loyalty_info

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.profile_loyalty_info.intent.ProfileLoyaltyInfoIntent
import ru.mercury.vpclient.features.profile_loyalty_info.model.ProfileLoyaltyInfoModel
import ru.mercury.vpclient.features.profile_loyalty_qr.navigation.ProfileLoyaltyQrRoute
import ru.mercury.vpclient.features.profile_loyalty_terms.navigation.ProfileLoyaltyTermsRoute
import ru.mercury.vpclient.shared.domain.usecase.CurrentUserUseCase
import ru.mercury.vpclient.shared.domain.usecase.DeleteLoyaltyCardUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyCardInfoFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyCardInfoUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyCardTypesUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileLoyaltyInfoViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val loyaltyCardInfoUseCase: LoyaltyCardInfoUseCase,
    private val loyaltyCardInfoFlowUseCase: LoyaltyCardInfoFlowUseCase,
    private val loyaltyCardTypesUseCase: LoyaltyCardTypesUseCase,
    private val deleteLoyaltyCardUseCase: DeleteLoyaltyCardUseCase
): ClientViewModel<ProfileLoyaltyInfoIntent, ProfileLoyaltyInfoModel, Event>(ProfileLoyaltyInfoModel()) {

    init {
        dispatch(ProfileLoyaltyInfoIntent.LoadData)
    }

    override fun dispatch(intent: ProfileLoyaltyInfoIntent) {
        when (intent) {
            is ProfileLoyaltyInfoIntent.LoadData -> {
                launch {
                    reduce { it.copy(isLoading = true) }
                    runCatching {
                        loyaltyCardInfoUseCase(Unit).getOrThrow()
                        val cardInfo = loyaltyCardInfoFlowUseCase(Unit).first()
                        val cardTypes = loyaltyCardTypesUseCase(Unit).getOrThrow()
                        cardInfo to cardTypes
                    }.onSuccess { result ->
                        reduce {
                            it.copy(
                                isLoading = false,
                                loyaltyCardInfoEntity = result.first,
                                cardTypes = result.second,
                                selectedCardType = result.first.typeCard
                            )
                        }
                    }.onFailure {
                        reduce { it.copy(isLoading = false) }
                    }
                }
            }
            is ProfileLoyaltyInfoIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is ProfileLoyaltyInfoIntent.QrClick -> {
                val qrCode = stateFlow.value.loyaltyCardInfoEntity.qrCode
                if (qrCode.isNotBlank()) {
                    launch { MainEventManager.send(ProfileLoyaltyQrRoute(qrCode = qrCode)) }
                }
            }
            is ProfileLoyaltyInfoIntent.MoreClick -> {
                val url = stateFlow.value.selectedCardDescription?.termsOfUse.orEmpty()
                if (url.isNotBlank()) {
                    launch { MainEventManager.send(ProfileLoyaltyTermsRoute(url = url)) }
                }
            }
            is ProfileLoyaltyInfoIntent.UnlinkClick -> reduce { it.copy(isUnlinkDialogVisible = true) }
            is ProfileLoyaltyInfoIntent.DismissUnlinkDialog -> reduce { it.copy(isUnlinkDialogVisible = false) }
            is ProfileLoyaltyInfoIntent.ConfirmUnlinkClick -> {
                val cardNumber = stateFlow.value.loyaltyCardInfoEntity.loyaltyCardNumber
                if (cardNumber.isBlank()) return

                launch {
                    reduce {
                        it.copy(
                            isUnlinkLoading = true,
                            isUnlinkDialogVisible = false
                        )
                    }
                    runCatching {
                        deleteLoyaltyCardUseCase(cardNumber).getOrThrow()
                        currentUserUseCase(Unit).getOrThrow()
                    }.onSuccess {
                        MainEventManager.send(BackRoute)
                    }.onFailure {
                        reduce { it.copy(isUnlinkLoading = false) }
                    }
                }
            }
            is ProfileLoyaltyInfoIntent.CardTypeClick -> reduce { it.copy(selectedCardType = intent.type) }
        }
    }
}
