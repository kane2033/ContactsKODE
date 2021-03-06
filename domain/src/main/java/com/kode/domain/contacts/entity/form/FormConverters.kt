package com.kode.domain.contacts.entity.form

import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.entity.PhoneNumber

fun Contact.toForm() = ContactForm(
    id = id,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber.toForm(),
    notes = notes,
    avatarUri = avatarUri,
    toneUri = toneUri
)

private fun PhoneNumber.toForm() = PhoneNumberForm(
    id = id,
    number = number,
    type = type
)