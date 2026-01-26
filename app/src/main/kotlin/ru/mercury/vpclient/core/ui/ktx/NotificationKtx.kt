package ru.mercury.vpclient.core.ui.ktx

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

val Context.notificationManager: NotificationManagerCompat
    get() = NotificationManagerCompat.from(this)

val Context.isPostNotificationsPermissionGranted: Boolean
    get() = when {
        Build.VERSION.SDK_INT >= 33 -> ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        else -> notificationManager.areNotificationsEnabled()
    }
