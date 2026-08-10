package ru.mercury.vpclient.shared.domain.usecase

import android.content.Context
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CheckoutSbpBank
import ru.mercury.vpclient.shared.data.network.SbpBanksNetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import javax.inject.Inject

class SbpAvailableBanksUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sbpBanksNetworkService: SbpBanksNetworkService,
    dispatchers: SharedDispatchers
): UseCase<Unit, List<CheckoutSbpBank>>(dispatchers.io) {

    override suspend fun execute(params: Unit): List<CheckoutSbpBank> {
        val response = try {
            sbpBanksNetworkService.banks()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw SbpAvailableBanksException(e.message.orEmpty())
        }

        return response.banks.orEmpty().mapNotNull { bank ->
            val bankName = bank.bankName?.trim().orEmpty()
            val packageName = bank.packageName?.trim().orEmpty()
            when {
                bankName.isEmpty() || packageName.isEmpty() || !isPackageInstalled(packageName) -> null
                else -> {
                    CheckoutSbpBank(
                        bankName = bankName,
                        logoUrl = bank.logoUrl.orEmpty(),
                        packageName = packageName
                    )
                }
            }
        }
    }

    private fun isPackageInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    data class SbpAvailableBanksException(
        override val message: String
    ): ClientException(message)
}
