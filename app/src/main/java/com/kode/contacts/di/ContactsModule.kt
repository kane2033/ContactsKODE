package com.kode.contacts.di

import com.kode.contacts.presentation.contacts.ContactsListViewModel
import com.kode.data.contacts.datasource.database.ContactsDataSourceImpl
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.usecase.CreateContact
import com.kode.domain.contacts.usecase.FetchContactsList
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ContactsModule {
    val module = module {
        single<ContactsDataSource> { ContactsDataSourceImpl(androidContext()) }
        single { FetchContactsList(get()) }
        single { CreateContact(get()) }

        viewModel { ContactsListViewModel(get(), get()) }
    }
}