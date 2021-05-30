package com.kode.domain.contacts.entity.form

import android.net.Uri
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.validation.constraint.ValidationConstraint

data class ContactForm(
    val id: Long = 0,
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: PhoneNumberForm = PhoneNumberForm(),
    var notes: String? = null,
    var avatarUri: Uri? = null,
    var toneUri: Uri? = null
) {
    private var avatarSource: AvatarSource? = null

    fun isAvatarStoredInGallery(): Boolean = avatarSource == AvatarSource.GALLERY

    fun setAvatarUriFrom(uri: Uri, source: AvatarSource) {
        avatarUri = uri
        avatarSource = source
    }

    fun isEmpty(): Boolean {
        return firstName.isNullOrBlank() and lastName.isNullOrBlank() and phoneNumber.isEmpty() and
                avatarUri?.path.isNullOrBlank() and toneUri?.path.isNullOrBlank()
    }

    fun isEqualsToContact(contact: Contact?): Boolean {
        return contact == toContact()
    }

    // Конвертация в общую доменную сущность
    // с валидацией полей
    fun toContact() = Contact(
        id = id,
        firstName = ValidationConstraint.NotEmpty.validate(firstName),
        lastName = lastName,
        phoneNumber = phoneNumber.toPhoneNumber(),
        notes = notes,
        avatarUri = avatarUri,
        toneUri = toneUri
    )

    enum class AvatarSource {
        GALLERY,
        CAMERA
    }
}