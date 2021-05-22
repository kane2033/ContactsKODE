package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.UseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact

class FetchContactsList(private val contactsDataSource: ContactsDataSource) :
    UseCase<List<Contact>, Unit>() {
    override fun run(param: Unit): List<Contact> {
        return contactsDataSource.getAllContacts()
    }
}