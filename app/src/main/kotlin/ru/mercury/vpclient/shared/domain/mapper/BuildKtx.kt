@file:Suppress("DEPRECATION")

package ru.mercury.vpclient.shared.domain.mapper

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import ru.mercury.vpclient.BuildConfig

val Context.versionCode: String
    get() {
        return try {
            val packageInfo = when {
                Build.VERSION.SDK_INT >= 33 -> packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
                else -> packageManager.getPackageInfo(packageName, 0)
            }
            val versionCode = when {
                Build.VERSION.SDK_INT >= 28 -> packageInfo.longVersionCode
                else -> packageInfo.versionCode.toLong()
            }
            versionCode.toString()
        } catch (_: Exception) {
            BuildConfig.VERSION_CODE.toString()
        }
    }
