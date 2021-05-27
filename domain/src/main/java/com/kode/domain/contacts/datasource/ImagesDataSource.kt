package com.kode.domain.contacts.datasource

interface ImagesDataSource {
    /**
     * Сохранение во внутреннее хранилище картинки по пути [externalImageFilePath].
     * @return Возвращает путь этой же картинки, сохраненной во внутреннем хранилище.
     * */
    fun saveImageToInternal(externalImageFilePath: String): String?
}