package com.kode.contacts.presentation.base.extension

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Context?.hasPermission(permission: String): Boolean {
    return this != null && ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

fun Fragment.permissionActivityResultContract(onGranted: () -> Unit, onRejected: () -> Unit) =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Разрешение получено
            onGranted()
        } else {
            // Разрешение не получено
            onRejected()
        }
    }