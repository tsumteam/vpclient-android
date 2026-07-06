package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CompilationDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import javax.inject.Inject

class CompilationEntitiesFlowUseCase @Inject constructor(
    private val compilationDao: CompilationDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, List<CompilationEntity>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<List<CompilationEntity>> {
        return compilationDao.selectAllFlow()
    }
}
