package com.kode.domain.contacts.usecase.image

import android.net.Uri
import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.FilesDataSource

class DeleteImage(private val filesDataSource: FilesDataSource) : UseCase<Uri, Unit>() {
    override suspend fun run(param: Uri) {
        filesDataSource.deleteFile(param)
    }
}