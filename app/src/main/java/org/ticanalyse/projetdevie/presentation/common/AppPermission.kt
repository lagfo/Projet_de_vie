package org.ticanalyse.projetdevie.presentation.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun AppPermission(context: Context) {
    val permissions = remember {
        mutableListOf(
            Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    val permissionStatuses = remember { mutableStateMapOf<String, Boolean>() }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var hasCheckedPermissions by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        result.forEach { (permission, isGranted) ->
            permissionStatuses[permission] = isGranted
        }

        // Vérifier s'il y a des permissions définitivement refusées
        val hasPermanentlyDeniedPermissions = permissions.any { permission ->
            !permissionStatuses[permission]!! &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
        }

        if (hasPermanentlyDeniedPermissions) {
            showSettingsDialog = true
        } else {
            val allPermissionsGranted = permissions.all { permissionStatuses[it] == true }
            if (allPermissionsGranted) {
                Toast.makeText(context, "Toutes les permissions accordées!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Vérifier les permissions au lancement
    LaunchedEffect(Unit) {
        permissions.forEach { permission ->
            permissionStatuses[permission] = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        hasCheckedPermissions = true
    }

    // Demander les permissions si nécessaire (uniquement après la vérification initiale)
    LaunchedEffect(hasCheckedPermissions, permissionStatuses.values) {
        if (hasCheckedPermissions) {
            val deniedPermissions = permissions.filter {
                permissionStatuses[it] == false
            }

            if (deniedPermissions.isNotEmpty()) {
                permissionLauncher.launch(deniedPermissions.toTypedArray())
            } else  {
                Toast.makeText(context, "Toutes les permissions sont accordées!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Autorisations requises") },
            text = {
                Text("Certaines autorisations sont définitivement refusées. Veuillez les activer dans les paramètres de l'application.")
            },
            confirmButton = {
                Button(onClick = {
                    showSettingsDialog = false
                    openAppSettings(context)
                }) {
                    Text("Ouvrir les paramètres")
                }
            },
            dismissButton = {
                Button(onClick = { showSettingsDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}