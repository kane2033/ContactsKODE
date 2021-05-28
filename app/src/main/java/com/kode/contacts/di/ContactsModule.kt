package com.kode.contacts.di

import com.kode.contacts.BuildConfig
import com.kode.contacts.presentation.contacts.details.ContactDetailsViewModel
import com.kode.contacts.presentation.contacts.edit.ContactEditViewModel
import com.kode.contacts.presentation.contacts.list.ContactsListViewModel
import com.kode.data.contacts.datasource.database.ContactsDataSourceImpl
import com.kode.data.contacts.datasource.database.ImagesDataSourceImpl
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.datasource.ImagesDataSource
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.usecase.CreateContact
import com.kode.domain.contacts.usecase.DeleteContact
import com.kode.domain.contacts.usecase.FetchContactById
import com.kode.domain.contacts.usecase.FetchContactsList
import com.kode.domain.contacts.usecase.image.CreateImageFile
import com.kode.domain.contacts.usecase.image.DeleteImage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ContactsModule {
    val module = module {
        single<ContactsDataSource> { ContactsDataSourceImpl(androidContext()) }
        single<ImagesDataSource> { ImagesDataSourceImpl(androidContext(), BuildConfig.AUTHORITIES) }

        single { FetchContactsList(get()) }
        single { FetchContactById(get()) }
        single { CreateContact(get(), get()) }
        single { DeleteContact(get()) }
        single { CreateImageFile(get()) }
        single { DeleteImage(get()) }

        viewModel { ContactsListViewModel(get()) }
        viewModel { ContactDetailsViewModel(get(), get()) }
        viewModel { (contact: Contact) ->
            ContactEditViewModel(contact, get(), get(), get(), get())
        }
    }
}