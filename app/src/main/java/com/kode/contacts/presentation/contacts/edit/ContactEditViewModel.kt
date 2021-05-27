package com.kode.contacts.presentation.contacts.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kode.contacts.presentation.base.extension.asLiveData
import com.kode.contacts.presentation.base.extension.handleFailure
import com.kode.contacts.presentation.base.extension.loadingIndication
import com.kode.domain.base.Event
import com.kode.domain.base.UiState
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.entity.form.ContactForm
import com.kode.domain.contacts.entity.form.toForm
import com.kode.domain.contacts.usecase.CreateContact
import com.kode.domain.contacts.usecase.DeleteContact
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class ContactEditViewModel(
    private val contact: Contact?,
    private val createContactUseCase: CreateContact,
    private val deleteContactUseCase: DeleteContact
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    // Сущность Contact, но со всеми полями var
    // (полезна для two day data binding)
    val contactForm = MutableLiveData(ContactForm())

    val mode: Mode

    init {
        if (contact != null) {
            contactForm.value = contact.toForm()
            mode = Mode.EDIT
        } else {
            mode = Mode.CREATE
        }
    }

    fun setAvatarPath(path: String) {
        contactForm.value?.avatarFilePath = path
    }

    fun hasUnsavedChanges() = when (mode) {
        Mode.CREATE -> contactForm.value?.isEmpty() == false
        Mode.EDIT -> contactForm.value?.isEqualsToContact(contact) == false
    }

    fun createContact(callback: () -> Unit) {
        val contactForm = contactForm.value ?: return
        viewModelScope.launch {
            createContactUseCase(contactForm).loadingIndication(_uiState).single().fold(
                onSuccess = { callback() },
                onFailure = { it.handleFailure(_uiState) }
            )
        }
    }

    fun deleteContact(callback: () -> Unit) {
        val contact = contact ?: return
        viewModelScope.launch {
            deleteContactUseCase(contact).loadingIndication(_uiState).single().fold(
                onSuccess = { callback() },
                onFailure = { it.handleFailure(_uiState) }
            )
        }
    }

    enum class Mode {
        CREATE, // Создание нового контакта
        EDIT // Редактирование существующего контакта
    }
}