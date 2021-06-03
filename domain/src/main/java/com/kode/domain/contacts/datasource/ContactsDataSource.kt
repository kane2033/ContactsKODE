package com.kode.domain.contacts.datasource

import com.kode.domain.contacts.entity.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsDataSource {
    fun getContact(id: Long): Flow<Contact>
    fun getAllContacts(): Flow<List<Contact>>
    suspend fun addContact(contact: Contact)
    suspend fun deleteContact(contact: Contact)
}