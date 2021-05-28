package com.kode.domain.contacts.datasource

import android.net.Uri
import com.kode.domain.contacts.entity.image.ExternalFileUri

interface ImagesDataSource {
    /**
     * Копирование во внутреннее хранилище картинки, хранящейся по [Uri] [externalImageUri].
     * @return Возвращает [Uri] этой же картинки, сохраненной во внутреннем хранилище.
     * */
    fun saveImageToInternal(externalImageUri: Uri): Uri?

    fun createImageFile(): ExternalFileUri

    fun deleteImageFile(uri: Uri)
}