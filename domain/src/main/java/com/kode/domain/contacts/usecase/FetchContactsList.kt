package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.FlowUseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact
import kotlinx.coroutines.flow.Flow

class FetchContactsList(private val contactsDataSource: ContactsDataSource) :
    FlowUseCase<Unit, List<Contact>>() {
    override fun run(param: Unit): Flow<List<Contact>> {
        return contactsDataSource.getAllContacts()
    }
}