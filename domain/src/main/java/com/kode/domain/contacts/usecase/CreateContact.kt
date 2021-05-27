package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.datasource.ImagesDataSource
import com.kode.domain.contacts.entity.form.ContactForm


class CreateContact(
    private val contactsDataSource: ContactsDataSource,
    private val imagesDataSource: ImagesDataSource
) : UseCase<ContactForm, Unit>() {
    override suspend fun run(param: ContactForm) {
        // Копируем картинку в память устройства
        // и сохраняем путь к картинке в поле контакта
        param.avatarFilePath?.let {
            param.avatarFilePath = imagesDataSource.saveImageToInternal(it)
        }
        // Перед добавлением в бд,
        // производится валидация в конвертации из ContactForm в Contact
        contactsDataSource.addContact(param.toContact())
    }
}