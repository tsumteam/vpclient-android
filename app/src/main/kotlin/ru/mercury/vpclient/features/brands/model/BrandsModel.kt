package ru.mercury.vpclient.features.brands.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.entity.BrandsPage
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model

data class BrandsModel(
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val selectedTab: TabType = TabType.WOMAN,
    val pages: List<BrandsPage> = TabType.entries.map { tab -> BrandsPage(tab = tab) },
    val favoriteBrandsText: String = "",
    val isFavoriteBrandsButtonVisible: Boolean = false,
    val loadBrandsJob: Job? = null,
    val saveFavoriteBrandJobs: Map<Int, Job> = emptyMap()
): Model {
    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val fittingText: String
        get() = if (fittingCount > 0) fittingCount.toString() else ""

    val isFittingButtonVisible: Boolean
        get() = fittingCount > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge
}
