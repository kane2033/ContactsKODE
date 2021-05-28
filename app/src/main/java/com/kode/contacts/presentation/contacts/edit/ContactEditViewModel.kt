package com.kode.contacts.presentation.contacts.edit

import android.net.Uri
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
import com.kode.domain.contacts.usecase.image.CreateImageFile
import com.kode.domain.contacts.usecase.image.DeleteImage
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class ContactEditViewModel(
    private val contact: Contact?,
    private val createContactUseCase: CreateContact,
    private val deleteContactUseCase: DeleteContact,
    private val createImageFileUseCase: CreateImageFile,
    private val deleteImageFileUseCase: DeleteImage
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    // Сущность Contact, но со всеми полями var
    // (полезна для two day data binding)
    val contactForm = MutableLiveData(ContactForm())

    val mode: Mode

    // Хранит uri файла нового фото
    // (take picture)
    var newPicImageUri: Uri? = null
        private set

    init {
        if (contact != null) {
            contactForm.value = contact.toForm()
            mode = Mode.EDIT
        } else {
            mode = Mode.CREATE
        }
    }

    fun setAvatarPathFromGallery(uri: Uri) {
        contactForm.value?.setAvatarUriFrom(uri, ContactForm.AvatarSource.GALLERY)
    }

    fun setAvatarPathFromCamera() {
        newPicImageUri?.let {
            contactForm.value?.setAvatarUriFrom(it, ContactForm.AvatarSource.CAMERA)
        }
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

    fun createImageFile(callback: (Uri) -> Unit) {
        viewModelScope.launch {
            createImageFileUseCase(Unit).loadingIndication(_uiState).single().fold(
                onSuccess = {
                    val (fileUri, providerUri) = it
                    newPicImageUri = fileUri // Uri файла непосредственно
                    callback(providerUri) // Uri, полученный через FileProvider (делимся файлом с камерой)
                },
                onFailure = { it.handleFailure(_uiState) }
            )
        }
    }

    fun deleteImageFile() {
        val uri = newPicImageUri ?: return
        newPicImageUri = null
        viewModelScope.launch {
            deleteImageFileUseCase(uri).loadingIndication(_uiState).single().onFailure {
                it.handleFailure(_uiState)
            }
        }
    }

    enum class Mode {
        CREATE, // Создание нового контакта
        EDIT // Редактирование существующего контакта
    }
}