package com.kode.domain.contacts.usecase.image

import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.FilesDataSource
import com.kode.domain.contacts.entity.image.SharedFileUri

class CreateImageFile(private val filesDataSource: FilesDataSource) :
    UseCase<Unit, SharedFileUri>() {
    override suspend fun run(param: Unit): SharedFileUri {
        return filesDataSource.createShareableFile(FilesDataSource.Directory.AVATARS)
    }
}