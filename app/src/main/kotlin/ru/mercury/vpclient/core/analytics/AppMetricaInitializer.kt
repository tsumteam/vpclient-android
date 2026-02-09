package ru.mercury.vpclient.core.analytics

import android.content.Context
import androidx.startup.Initializer

class AppMetricaInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        /*val config = AppMetricaConfig
            .newConfigBuilder(BuildConfig.APPMETRICA_API_KEY)
            .withLogs()
            .build()
        AppMetrica.activate(context, config)
        AppMetrica.enableActivityAutoTracking(context.applicationContext as Application)
        AppMetrica.setDataSendingEnabled(true)*/
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
