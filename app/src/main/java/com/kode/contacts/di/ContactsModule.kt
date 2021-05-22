package com.kode.contacts.di

import com.kode.contacts.presentation.contacts.ContactsListViewModel
import com.kode.data.contacts.datasource.database.ContactsDataSourceMock
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.usecase.FetchContactsList
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ContactsModule {
    val module = module {
        single<ContactsDataSource> { ContactsDataSourceMock() }
        single { FetchContactsList(get()) }

        viewModel { ContactsListViewModel(get()) }
    }
}