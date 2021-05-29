package com.kode.contacts.di

import com.kode.contacts.BuildConfig
import com.kode.contacts.presentation.contacts.details.ContactDetailsViewModel
import com.kode.contacts.presentation.contacts.edit.ContactEditViewModel
import com.kode.contacts.presentation.contacts.list.ContactsListViewModel
import com.kode.data.contacts.datasource.database.ContactsDataSourceImpl
import com.kode.data.contacts.datasource.database.FilesDataSourceImpl
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.datasource.FilesDataSource
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.usecase.*
import com.kode.domain.contacts.usecase.image.CreateImageFile
import com.kode.domain.contacts.usecase.image.DeleteImage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ContactsModule {
    val module = module {
        single<ContactsDataSource> { ContactsDataSourceImpl(androidContext()) }
        single<FilesDataSource> { FilesDataSourceImpl(androidContext(), BuildConfig.AUTHORITIES) }

        single { FetchContactsList(get()) }
        single { SearchContacts(get()) }
        single { FetchContactById(get()) }
        single { CreateContact(get(), get()) }
        single { DeleteContact(get()) }
        single { CreateImageFile(get()) }
        single { DeleteImage(get()) }

        viewModel { ContactsListViewModel(get(), get()) }
        viewModel { ContactDetailsViewModel(get(), get()) }
        viewModel { (contact: Contact) ->
            ContactEditViewModel(contact, get(), get(), get(), get())
        }
    }
}