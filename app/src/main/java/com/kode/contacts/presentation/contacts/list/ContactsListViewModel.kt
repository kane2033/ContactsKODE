package com.kode.contacts.presentation.contacts.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kode.contacts.presentation.base.extension.asLiveData
import com.kode.contacts.presentation.base.extension.handleFailure
import com.kode.contacts.presentation.base.extension.loadingIndication
import com.kode.domain.base.Event
import com.kode.domain.base.UiState
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.usecase.FetchContactsList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactsListViewModel(private val fetchContactsList: FetchContactsList) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts = _contacts.asLiveData()

    init {
        fetchContacts()
    }

    private fun fetchContacts() {
        viewModelScope.launch {
            fetchContactsList(Unit).loadingIndication(_uiState).collect { result ->
                result.fold(
                    onSuccess = { _contacts.value = it },
                    onFailure = { it.handleFailure(_uiState) }
                )
            }
        }
    }
}