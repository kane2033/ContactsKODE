package com.kode.data.contacts.datasource.database

import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact

class ContactsDataSourceMock: ContactsDataSource {
    override fun getAllContacts(): List<Contact> {
        return listOf(
            Contact("Akakiy 1"),
            Contact("Akakiy 2"),
            Contact("Akakiy 3"),
            Contact("Akakiy 4"),
            Contact("Akakiy 5"),
        )
    }
}