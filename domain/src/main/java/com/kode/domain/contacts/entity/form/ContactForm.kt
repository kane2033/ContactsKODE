package com.kode.domain.contacts.entity.form

import com.kode.domain.contacts.entity.Contact
import com.kode.domain.validation.constraint.ValidationConstraint

data class ContactForm(
    val id: Long = 0,
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: PhoneNumberForm = PhoneNumberForm(),
    var avatarFilePath: String? = null,
    var toneFilePath: String? = null
) {
    fun isEmpty(): Boolean {
        return firstName.isNullOrBlank() and lastName.isNullOrBlank() and phoneNumber.isEmpty() and
                avatarFilePath.isNullOrBlank() and toneFilePath.isNullOrBlank()
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
        phoneNumbers = listOf(
            phoneNumber.toPhoneNumber()
        ),
        avatarFilePath = avatarFilePath,
        toneFilePath = toneFilePath
    )
}