package com.kode.domain.contacts.datasource

import android.net.Uri
import com.kode.domain.contacts.entity.image.SharedFileUri

interface FilesDataSource {
    /**
     * Копирование во внутреннее хранилище файла, хранящегося по [Uri] [fileUri].
     * @return Возвращает [Uri] этого же файла, сохраненного во внутреннем хранилище.
     * */
    fun saveFileToInternal(fileUri: Uri, directory: Directory? = null): Uri?

    fun createShareableFile(directory: Directory? = null): SharedFileUri

    fun deleteFile(uri: Uri)

    enum class Directory {
        AVATARS,
        RINGTONES
    }
}