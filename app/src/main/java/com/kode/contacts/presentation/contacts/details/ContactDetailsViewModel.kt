package com.kode.contacts.presentation.contacts.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kode.contacts.presentation.base.extension.asLiveData
import com.kode.contacts.presentation.base.extension.handleFailure
import com.kode.contacts.presentation.base.extension.loadingIndication
import com.kode.domain.base.Event
import com.kode.domain.base.UiState
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.usecase.FetchContactById
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactDetailsViewModel(
    private val contactId: Long,
    private val fetchContactById: FetchContactById
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    private val _contact = MutableLiveData<Contact>()
    val contact = _contact.asLiveData()

    init {
        fetchSelectedContact()
    }

    private fun fetchSelectedContact() {
        viewModelScope.launch {
            fetchContactById(contactId).loadingIndication(_uiState).collect { result ->
                result.fold(
                    onSuccess = { _contact.value = it },
                    onFailure = { it.handleFailure(_uiState) }
                )
            }
        }
    }
}