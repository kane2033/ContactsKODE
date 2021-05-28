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
        // Если аватар хранится в галерее (внешяя память),
        // необходимо сохранить его во внутреннюю память приложения
        val avatarFilePath = param.avatarUri
        if (avatarFilePath != null && param.isAvatarStoredInGallery()) {
            param.avatarUri = imagesDataSource.saveImageToInternal(avatarFilePath)
        }

        // Перед добавлением в бд,
        // производится валидация в конвертации из ContactForm в Contact
        contactsDataSource.addContact(param.toContact())
    }
}