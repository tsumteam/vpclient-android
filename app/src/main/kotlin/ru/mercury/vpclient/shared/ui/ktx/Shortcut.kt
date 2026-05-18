package ru.mercury.vpclient.shared.ui.ktx

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.shared.ui.theme.ClientIcons
import ru.mercury.vpclient.features.debug.DebugActivity

fun Context.installShortcuts() {
    if (BuildConfig.DEBUG) {
        val intent = Intent(this, DebugActivity::class.java).apply {
            action = "$packageName.DEBUG_SETTINGS"
            addCategory(Intent.CATEGORY_DEFAULT)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        }
        val settingsShortcut = ShortcutInfoCompat.Builder(this, "settingsShortcutId")
            .setShortLabel("Debug Настройки")
            .setLongLabel("Debug Настройки")
            .setRank(1)
            .setIcon(IconCompat.createWithResource(this, ClientIcons.ShortcutSettins))
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.setDynamicShortcuts(this, listOf(settingsShortcut))
    }
}

fun Context.reportShortcutUsed() {
    ShortcutManagerCompat.reportShortcutUsed(this, "settingsShortcutId")
}
