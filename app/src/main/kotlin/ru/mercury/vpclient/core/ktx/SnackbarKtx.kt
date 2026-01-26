package ru.mercury.vpclient.core.ktx

import android.content.Context
import ru.mercury.vpclient.core.event.BaseSnackbarEvent

fun BaseSnackbarEvent.snackbarMessage(context: Context): String {
    return when {
        message is Int -> context.getString(message as Int)
        else -> message.toString()
    }
}
