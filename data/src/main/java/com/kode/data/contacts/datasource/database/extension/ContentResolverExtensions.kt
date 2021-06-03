package com.kode.data.contacts.datasource.database.extension

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun ContentResolver.getFileName(fileUri: Uri): String? {
    return when (fileUri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getContentFileName(fileUri)
        else -> fileUri.getFileName()
    }
}

private fun ContentResolver.getContentFileName(fileUri: Uri): String? {
    return query(fileUri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        cursor.getString(nameIndex)
    }
}

private fun Uri.getFileName() = path?.let { File(it).name }