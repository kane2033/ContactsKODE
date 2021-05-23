package com.kode.contacts.presentation.contacts.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kode.contacts.presentation.base.adapter.ItemClickedInterface
import com.kode.contacts.presentation.base.extension.asLiveData
import com.kode.domain.base.Event
import com.kode.domain.base.UiState
import com.kode.domain.contacts.entity.Contact

class ContactDetailsViewModel(contact: Contact) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    val contact: LiveData<Contact> = liveData { emit(contact) }

    val onContactClicked = ItemClickedInterface<Contact> {

    }
}