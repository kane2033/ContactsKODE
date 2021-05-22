package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.FlowUseCase
import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact
import kotlinx.coroutines.flow.Flow

class CreateContact(private val contactsDataSource: ContactsDataSource): UseCase<Contact, Unit>() {
    override suspend fun run(param: Contact) {
        contactsDataSource.addContact(param)
    }
}