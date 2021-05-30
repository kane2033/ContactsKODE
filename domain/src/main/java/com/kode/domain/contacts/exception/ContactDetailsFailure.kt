package com.kode.domain.contacts.exception

import com.kode.domain.base.exception.Failure

sealed class ContactDetailsFailure : Failure.FeatureFailure() {
    object PhoneIncorrect : ContactDetailsFailure()
}