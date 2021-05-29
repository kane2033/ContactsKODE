package com.kode.domain.contacts.entity.form

import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.entity.PhoneNumber

fun Contact.toForm() = ContactForm(
    id = id,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumbers[0].toForm(),
    avatarUri = avatarUri,
    toneUri = toneUri
)

private fun PhoneNumber.toForm() = PhoneNumberForm(
    id = id,
    number = number,
    type = type
)