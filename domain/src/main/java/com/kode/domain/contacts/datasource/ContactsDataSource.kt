package com.kode.domain.contacts.datasource

import com.kode.domain.contacts.entity.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsDataSource {
    fun getAllContacts(): Flow<List<Contact>>
    suspend fun addContactsList(contacts: List<Contact>)
    suspend fun addContact(contact: Contact)
    suspend fun deleteContact(contact: Contact)
}