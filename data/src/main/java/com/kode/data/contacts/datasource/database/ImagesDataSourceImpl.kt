package com.kode.data.contacts.datasource.database

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.kode.domain.contacts.datasource.ImagesDataSource
import com.kode.domain.contacts.entity.image.ExternalFileUri
import java.io.File
import java.io.FileOutputStream


class ImagesDataSourceImpl(
    private val context: Context,
    private val authorities: String
) : ImagesDataSource {

    companion object {
        private const val IMAGES_DIRECTORY = "avatars"
    }

    override fun saveImageToInternal(externalImageUri: Uri): Uri? {

        val fileName = generateFileName()
        val file = File(context.getDir(IMAGES_DIRECTORY, Context.MODE_PRIVATE), fileName)

        val outputStream = FileOutputStream(file)
        val inputStream = context.contentResolver.openInputStream(externalImageUri) ?: return null

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return file.toUri()
    }

    // Создание файла во внешнем хранилище
    override fun createImageFile(): ExternalFileUri {
        val fileName = generateFileName()
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        val fileProviderUri = FileProvider.getUriForFile(
            context,
            authorities,
            file
        )
        return ExternalFileUri(fileUri = file.toUri(), fileProviderUri = fileProviderUri)
    }

    // Удаление файла из внешнего хранилища
    override fun deleteImageFile(uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }

    private fun generateFileName() = "img_${System.currentTimeMillis()}.jpg"
}