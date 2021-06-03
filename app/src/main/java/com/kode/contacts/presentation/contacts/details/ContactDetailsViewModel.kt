package com.kode.contacts.presentation.contacts.details

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
import com.kode.domain.contacts.exception.ContactDetailsFailure
import com.kode.domain.contacts.usecase.FetchContactById
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ContactDetailsViewModel(
    private val contactId: Long,
    private val fetchContactById: FetchContactById
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    private val _contact = MutableLiveData<Contact>()
    val contact = _contact.asLiveData()

    fun setPhoneIncorrectFailure() {
        _uiState.value = Event(UiState.Failure(ContactDetailsFailure.PhoneIncorrect))
    }

    fun getPhoneNumber(): String? = contact.value?.phoneNumber?.number

    init {
        fetchSelectedContact()
    }

    private fun fetchSelectedContact() {
        viewModelScope.launch {
            fetchContactById(contactId).onStart { _uiState.setLoading() }.collect { result ->
                result.fold(
                    onSuccess = {
                        _uiState.setSuccess()
                        _contact.value = it
                    },
                    onFailure = { it.handleFailure(_uiState) }
                )
            }
        }
    }
}