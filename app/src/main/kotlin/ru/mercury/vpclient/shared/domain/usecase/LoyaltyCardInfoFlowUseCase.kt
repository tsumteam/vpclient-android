package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.LoyaltyCardInfoDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.LoyaltyCardInfoEntity
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import javax.inject.Inject

class LoyaltyCardInfoFlowUseCase @Inject constructor(
    private val loyaltyCardInfoDao: LoyaltyCardInfoDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, LoyaltyCardInfoEntity>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<LoyaltyCardInfoEntity> {
        return loyaltyCardInfoDao.selectFlow().map { entity -> entity.orEmpty }
    }

    companion object {
        const val ALPHA_BANK_DISCLAIMER_HIDE_DURATION_MILLIS = 604_800_000L // 7L * 24L * 60L * 60L * 1000L
    }
}
