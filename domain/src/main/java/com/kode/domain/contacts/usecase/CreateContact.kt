package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.form.ContactForm


class CreateContact(private val contactsDataSource: ContactsDataSource) :
    UseCase<ContactForm, Unit>() {
    override suspend fun run(param: ContactForm) {
        // Валидация полей происходит при конвертации из ContactForm в Contact
        contactsDataSource.addContact(param.toContact())
    }
}