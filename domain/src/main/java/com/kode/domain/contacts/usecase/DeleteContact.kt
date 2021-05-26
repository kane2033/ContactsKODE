package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact

class DeleteContact(private val dataSource: ContactsDataSource) : UseCase<Contact, Unit>() {
    override suspend fun run(param: Contact) {
        return dataSource.deleteContact(param)
    }
}