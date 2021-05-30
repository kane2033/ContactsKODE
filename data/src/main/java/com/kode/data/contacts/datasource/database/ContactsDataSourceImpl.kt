package com.kode.data.contacts.datasource.database

import android.content.Context
import com.kode.data.contacts.datasource.database.converter.toDbEntity
import com.kode.data.contacts.datasource.database.converter.toDbEntityList
import com.kode.data.contacts.datasource.database.converter.toDomainEntity
import com.kode.data.contacts.datasource.database.converter.toDomainEntityList
import com.kode.data.contacts.datasource.database.db.ContactsDatabase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContactsDataSourceImpl(context: Context) : ContactsDataSource {

    private val database = ContactsDatabase.getInstance(context)

    override fun getContact(id: Long): Flow<Contact> {
        return database.contactDao().getContactById(id).map { it.toDomainEntity() }
    }

    override fun getAllContacts(): Flow<List<Contact>> {
        return database.contactDao().getAllByFirstName()
            .map { it.toDomainEntityList() }
    }

    override suspend fun addContactsList(contacts: List<Contact>) {
        return database.contactDao().insertContactsListWithPhone(contacts.toDbEntityList())
    }

    override suspend fun addContact(contact: Contact) {
        return database.contactDao().insertContactWithPhone(contact.toDbEntity())
    }

    override suspend fun deleteContact(contact: Contact) {
        return database.contactDao().deleteContactWithPhones(contact.toDbEntity())
    }
}