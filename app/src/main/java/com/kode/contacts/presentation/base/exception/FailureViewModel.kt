package com.kode.contacts.presentation.base.exception

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class FailureViewModel(failureInfo: FailureInfo.FullScreen) : ViewModel() {

    val failureInfo: LiveData<FailureInfo.FullScreen> = liveData { emit(failureInfo) }
}