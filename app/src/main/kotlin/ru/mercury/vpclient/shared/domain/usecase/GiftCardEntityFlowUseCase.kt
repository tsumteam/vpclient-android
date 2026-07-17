@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.data.persistence.database.dao.GiftCardDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardEntity
import javax.inject.Inject

class GiftCardEntityFlowUseCase @Inject constructor(
    private val giftCardDao: GiftCardDao,
    dispatchers: SharedDispatchers
): FlowUseCase<GiftCardType, GiftCardEntity?>(dispatchers.io) {

    override fun execute(type: GiftCardType): Flow<GiftCardEntity?> {
        return giftCardDao.selectFlow(type)
    }
}
