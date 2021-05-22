package com.kode.domain.contacts.datasource

import com.kode.domain.contacts.entity.Contact

interface ContactsDataSource {
    fun getAllContacts(): List<Contact>
}