package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.FlowUseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact
import kotlinx.coroutines.flow.Flow

class FetchContactById(private val contactsDataSource: ContactsDataSource) :
    FlowUseCase<Long, Contact>() {
    override fun run(param: Long): Flow<Contact> {
        return contactsDataSource.getContact(param)
    }
}