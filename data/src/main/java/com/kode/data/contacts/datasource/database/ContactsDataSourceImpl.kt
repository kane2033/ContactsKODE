package com.kode.data.contacts.datasource.database

import com.kode.data.contacts.datasource.database.converter.toDbEntity
import com.kode.data.contacts.datasource.database.converter.toDbEntityList
import com.kode.data.contacts.datasource.database.converter.toDomainEntity
import com.kode.data.contacts.datasource.database.converter.toDomainEntityList
import com.kode.data.contacts.datasource.database.dao.ContactDao
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContactsDataSourceImpl(private val dao: ContactDao) : ContactsDataSource {

    override fun getContact(id: Long): Flow<Contact> {
        return dao.getContactById(id).map { it.toDomainEntity() }
    }

    override fun getAllContacts(): Flow<List<Contact>> {
        return dao.getAllByFirstName()
            .map { it.toDomainEntityList() }
    }

    override suspend fun addContactsList(contacts: List<Contact>) {
        return dao.insertContactsListWithPhone(contacts.toDbEntityList())
    }

    override suspend fun addContact(contact: Contact) {
        return dao.insertContactWithPhone(contact.toDbEntity())
    }

    override suspend fun deleteContact(contact: Contact) {
        return dao.deleteContactWithPhones(contact.toDbEntity())
    }
}