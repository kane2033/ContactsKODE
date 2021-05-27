package com.kode.data.contacts.datasource.database

import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import com.kode.domain.contacts.datasource.ImagesDataSource
import java.io.File
import java.io.FileOutputStream

class ImagesDataSourceImpl(context: Context) : ImagesDataSource {

    companion object {
        private const val IMAGES_DIRECTORY = "avatars"
    }

    private val fileDirectory =
        ContextWrapper(context).getDir(IMAGES_DIRECTORY, Context.MODE_PRIVATE)

    private val contentResolver = context.contentResolver

    override fun saveImageToInternal(externalImageFilePath: String): String? {
        val externalImageUri = Uri.parse(externalImageFilePath)

        val fileName = generateFileName()
        val file = File(fileDirectory, fileName)

        val outputStream = FileOutputStream(file)
        val inputStream = contentResolver.openInputStream(externalImageUri) ?: return null

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return file.absolutePath
    }

    private fun generateFileName() = "img_" + System.currentTimeMillis()
}