package com.kode.domain.contacts.usecase.image

import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.ImagesDataSource
import com.kode.domain.contacts.entity.image.ExternalFileUri

class CreateImageFile(private val imagesDataSource: ImagesDataSource) :
    UseCase<Unit, ExternalFileUri>() {
    override suspend fun run(param: Unit): ExternalFileUri {
        return imagesDataSource.createImageFile()
    }
}