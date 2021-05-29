package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.datasource.FilesDataSource
import com.kode.domain.contacts.entity.form.ContactForm


class CreateContact(
    private val contactsDataSource: ContactsDataSource,
    private val filesDataSource: FilesDataSource
) : UseCase<ContactForm, Unit>() {
    override suspend fun run(param: ContactForm) {
        // Если аватар хранится в галерее (внешяя память),
        // необходимо сохранить его во внутреннюю память приложения
        val avatarFilePath = param.avatarUri
        if (avatarFilePath != null && param.isAvatarStoredInGallery()) {
            param.avatarUri = filesDataSource.saveFileToInternal(
                avatarFilePath,
                FilesDataSource.Directory.AVATARS
            )
        }
        // Аналогично с рингтоном
        param.toneUri?.let {
            param.toneUri =
                filesDataSource.saveFileToInternal(it, FilesDataSource.Directory.RINGTONES)
        }

        // Перед добавлением в бд,
        // производится валидация в конвертации из ContactForm в Contact
        contactsDataSource.addContact(param.toContact())
    }
}