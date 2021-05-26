package com.kode.contacts.di

import com.kode.contacts.presentation.contacts.details.ContactDetailsViewModel
import com.kode.contacts.presentation.contacts.edit.ContactEditViewModel
import com.kode.contacts.presentation.contacts.list.ContactsListViewModel
import com.kode.data.contacts.datasource.database.ContactsDataSourceImpl
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.usecase.CreateContact
import com.kode.domain.contacts.usecase.DeleteContact
import com.kode.domain.contacts.usecase.FetchContactById
import com.kode.domain.contacts.usecase.FetchContactsList
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ContactsModule {
    val module = module {
        single<ContactsDataSource> { ContactsDataSourceImpl(androidContext()) }

        single { FetchContactsList(get()) }
        single { FetchContactById(get()) }
        single { CreateContact(get()) }
        single { DeleteContact(get()) }

        viewModel { ContactsListViewModel(get()) }
        viewModel { ContactDetailsViewModel(get(), get()) }
        viewModel { (contact: Contact) -> ContactEditViewModel(contact, get(), get()) }
    }
}