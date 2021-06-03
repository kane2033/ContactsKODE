package com.kode.contacts.presentation.contacts.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kode.contacts.presentation.base.entity.Event
import com.kode.contacts.presentation.base.entity.UiState
import com.kode.contacts.presentation.base.extension.asLiveData
import com.kode.contacts.presentation.base.extension.handleFailure
import com.kode.contacts.presentation.base.extension.setLoading
import com.kode.contacts.presentation.base.extension.setSuccess
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.usecase.FetchContactsList
import com.kode.domain.contacts.usecase.SearchContacts
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ContactsListViewModel(
    private val fetchContactsList: FetchContactsList,
    private val searchContactsUseCase: SearchContacts
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts = _contacts.asLiveData()

    private var unfilteredContacts = listOf<Contact>()

    init {
        fetchContacts()
    }

    private fun fetchContacts() {
        viewModelScope.launch {
            val flow = fetchContactsList(Unit).onStart { _uiState.setLoading() }
            flow.collect { result ->
                result.fold(
                    onSuccess = {
                        _uiState.setSuccess()
                        _contacts.value = it
                        unfilteredContacts = it
                    },
                    onFailure = { it.handleFailure(_uiState) }
                )
            }
        }
    }

    fun searchContacts(constraint: String) {
        viewModelScope.launch {
            searchContactsUseCase(constraint).onStart { _uiState.setLoading() }.collect { result ->
                result.fold(
                    onSuccess = {
                        _uiState.setSuccess()
                        _contacts.value = it
                    },
                    onFailure = { it.handleFailure(_uiState) }
                )
            }
        }
    }
}