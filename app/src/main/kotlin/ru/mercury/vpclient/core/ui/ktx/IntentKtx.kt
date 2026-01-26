package ru.mercury.vpclient.core.ui.ktx

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

@Composable
fun rememberNavigateToAppSettings(): () -> Unit {
    val context = LocalContext.current
    val appSettingsContract = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        "package:${context.packageName}".toUri()
    ).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return remember { { appSettingsContract.launch(intent) } }
}

@Composable
fun rememberNavigateToDeveloperSettings(): () -> Unit {
    val appSettingsContract = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    return remember { { appSettingsContract.launch(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)) } }
}

@Composable
fun rememberRequestCameraPermission(
    onGranted: () -> Unit
): () -> Unit {
    val context = LocalContext.current
    val activity = requireNotNull(LocalActivity.current)
    val navigateToAppSettings = rememberNavigateToAppSettings()
    val cameraPermissionContract = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val shouldRequest = activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
        when {
            granted -> onGranted()
            !granted && !shouldRequest -> navigateToAppSettings()
        }
    }
    return remember {
        {
            when {
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED -> {
                    cameraPermissionContract.launch(Manifest.permission.CAMERA)
                }
                else -> onGranted()
            }
        }
    }
}

@Composable
fun rememberRequestMultiplePermissions(): () -> Unit {
    val permissions = when {
        Build.VERSION.SDK_INT >= 33 -> arrayOf(Manifest.permission.CAMERA, Manifest.permission.POST_NOTIFICATIONS)
        else -> arrayOf(Manifest.permission.CAMERA)
    }
    val context = LocalContext.current
    val multiplePermissionsContract = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {}

    return remember {
        {
            val hasMissingPermission = permissions.any { permission ->
                ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
            }
            when {
                hasMissingPermission -> multiplePermissionsContract.launch(permissions)
                else -> Unit
            }
        }
    }
}
