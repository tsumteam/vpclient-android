@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CompilationPreviewPageDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity
import javax.inject.Inject

class CompilationPreviewPageEntitiesFlowUseCase @Inject constructor(
    private val compilationPreviewPageDao: CompilationPreviewPageDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Int, List<CompilationPreviewPageEntity>>(dispatchers.io) {

    override fun execute(compilationId: Int): Flow<List<CompilationPreviewPageEntity>> {
        return compilationPreviewPageDao.selectFlow(compilationId)
    }
}
