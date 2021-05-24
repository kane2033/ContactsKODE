package com.kode.contacts.presentation.contacts.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kode.contacts.presentation.base.extension.asLiveData
import com.kode.domain.base.Event
import com.kode.domain.base.UiState
import com.kode.domain.contacts.entity.Contact

class ContactEditViewModel(contact: Contact?) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    private val _contact = MutableLiveData<Contact>()
    val contact = _contact.asLiveData()

    val mode: Mode

    init {
        if (contact != null) {
            contact.also { _contact.value = it } // при _contact.value = contact ошибка (баг)
            mode = Mode.EDIT
        } else {
            mode = Mode.CREATE
        }
    }

    enum class Mode {
        CREATE, // Создание нового контакта
        EDIT // Редактирование существующего контакта
    }
}