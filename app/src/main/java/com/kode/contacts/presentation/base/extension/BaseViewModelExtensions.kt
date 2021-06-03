package com.kode.contacts.presentation.base.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kode.contacts.presentation.base.entity.Event
import com.kode.contacts.presentation.base.entity.UiState
import com.kode.domain.base.exception.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

// Поток запускается - отображается загрузка
// Поток останавливается - загрузка не показывается
fun <T> Flow<T>.loadingIndication(uiState: MutableLiveData<Event<UiState>>) =
    onStart { uiState.value = Event(UiState.Loading) }
        .onCompletion { uiState.value = Event(UiState.Success) }

fun MutableLiveData<Event<UiState>>.setLoading() {
    value = Event(UiState.Loading)
}

fun MutableLiveData<Event<UiState>>.setSuccess() {
    value = Event(UiState.Success)
}

// Стандартный метод обработки ошибки, упаковывающий ее
// в event
fun Throwable.handleFailure(uiState: MutableLiveData<Event<UiState>>) {
    if (this is Failure) {
        uiState.value = Event(UiState.Failure(this))
    }
}

// Создание LiveData из MutableLiveData,
// чтобы скрыть MutableLiveData от view
fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>