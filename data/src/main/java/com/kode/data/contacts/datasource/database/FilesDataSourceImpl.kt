package com.kode.data.contacts.datasource.database

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.kode.data.contacts.datasource.database.extension.getFileName
import com.kode.domain.contacts.datasource.FilesDataSource
import com.kode.domain.contacts.entity.image.SharedFileUri
import java.io.File
import java.io.FileOutputStream


class FilesDataSourceImpl(
    private val context: Context,
    private val authorities: String
) : FilesDataSource {

    private fun parseDirectoryEnum(directory: FilesDataSource.Directory?) = when (directory) {
        FilesDataSource.Directory.AVATARS -> "avatars"
        FilesDataSource.Directory.RINGTONES -> "ringtones"
        else -> ""
    }

    override fun saveFileToInternal(
        fileUri: Uri,
        directory: FilesDataSource.Directory?
    ): Uri? {
        val fileName =
            context.contentResolver.getFileName(fileUri) ?: generateFileName(directory)
        val directoryString = parseDirectoryEnum(directory)
        val file = File(context.getDir(directoryString, Context.MODE_PRIVATE), fileName)

        // Если это не внешний файл и уже сохранялся во
        // внутренней папке
        if (file.exists()) return fileUri

        val outputStream = FileOutputStream(file)
        val inputStream = context.contentResolver.openInputStream(fileUri) ?: return null

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return file.toUri()
    }

    // Создание файла во внешнем хранилище
    override fun createShareableFile(directory: FilesDataSource.Directory?): SharedFileUri {
        val fileName = generateFileName(directory)
        val directoryString = parseDirectoryEnum(directory)
        val file = File(context.getExternalFilesDir(directoryString), fileName)
        val fileProviderUri = FileProvider.getUriForFile(
            context,
            authorities,
            file
        )
        return SharedFileUri(fileUri = file.toUri(), sharedByProviderUri = fileProviderUri)
    }

    // Удаление файла из внешнего хранилища
    override fun deleteFile(uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }

    private fun generateFileName(directory: FilesDataSource.Directory?) = when (directory) {
        FilesDataSource.Directory.AVATARS -> "img_${System.currentTimeMillis()}.jpg"
        FilesDataSource.Directory.RINGTONES -> "ringtone_${System.currentTimeMillis()}.ogg"
        null -> "file_${System.currentTimeMillis()}"
    }
}