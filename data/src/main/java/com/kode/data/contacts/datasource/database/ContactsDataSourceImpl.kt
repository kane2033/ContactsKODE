package com.kode.data.contacts.datasource.database

import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact

class ContactsDataSourceImpl: ContactsDataSource {
    override fun getAllContacts(): List<Contact> {
        return emptyList()
    }
}