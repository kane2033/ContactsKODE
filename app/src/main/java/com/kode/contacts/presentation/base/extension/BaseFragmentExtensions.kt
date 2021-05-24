package com.kode.contacts.presentation.base.extension

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kode.contacts.R
import com.kode.contacts.presentation.base.exception.FailureFragmentDirections
import com.kode.domain.base.Event
import com.kode.domain.base.UiState
import com.kode.domain.base.exception.Failure
import com.kode.domain.base.exception.info.FailureInfo
import com.kode.domain.base.exception.info.FullScreenFailureInfo
import com.kode.domain.base.exception.info.SmallFailureInfo

fun Fragment.makeAlertDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    positiveCallback: () -> Unit,
    negativeCallback: () -> Unit,
    @StringRes positiveText: Int = R.string.yes,
    @StringRes negativeText: Int = R.string.no,
) {
    makeAlertDialog(
        title = getString(title),
        message = getString(message),
        positiveText = getString(positiveText),
        positiveCallback = positiveCallback,
        negativeText = getString(negativeText),
        negativeCallback = negativeCallback
    )
}

fun Fragment.makeAlertDialog(
    title: String,
    message: String,
    positiveCallback: () -> Unit,
    negativeCallback: () -> Unit,
    positiveText: String = getString(R.string.yes),
    negativeText: String = getString(R.string.no),
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { _, _ -> positiveCallback() }
        .setNegativeButton(negativeText) { _, _ -> negativeCallback() }
        .show()
}

// Отображение Toast уведомления со строкой из ресурсов
fun Fragment.makeToast(@StringRes message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// Отображение Toast уведомления с любой строкой
fun Fragment.makeToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.makeSnackBar(
    @StringRes messageRes: Int,
    @StringRes actionTextRes: Int? = null,
    action: (() -> Unit)? = null,
    view: View? = null
) {
    val message = getString(messageRes)
    val actionText = if (actionTextRes != null) getString(actionTextRes) else null
    makeSnackBar(message, actionText, action, view)
}

fun Fragment.makeSnackBar(
    message: String,
    actionText: String? = null,
    action: (() -> Unit)? = null,
    view: View? = null
) {
    val snackBar = Snackbar.make(view ?: this.requireView(), message, Snackbar.LENGTH_SHORT)
    action?.let { snackBar.setAction(actionText) { it() } }
    snackBar.show()
}

/**
 * Освобождает от необходимости писать getContentIfNotHandled для [LiveData] с типом [Event]:
 * @param [observer] выполнится, только если ивент еще не обработан.
 * */
//
fun <T> LiveData<Event<T>>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    observer: (value: T) -> Unit
) {
    observe(lifecycleOwner, { event ->
        event.getContentIfNotHandled()?.let { content -> observer(content) }
    })
}

/**
 * Наблюдение за [UiState.Failure].
 * */
fun LiveData<Event<UiState>>.observeFailure(
    lifecycleOwner: LifecycleOwner,
    observer: (value: Failure) -> Unit
) {
    observeEvent(lifecycleOwner, { uiState ->
        if (uiState is UiState.Failure) observer(uiState.failure)
    })
}

/**
 * Функция открывает окно ошибки в зависимости от переданного параметра [failure].
 * Если переданный параметр [failure]
 * не обработан, отображает стандартное сообщение ошибки.
 *
 * В зависимости от возвращенного типа ([FullScreenFailureInfo] или [SmallFailureInfo])
 * открывается соответствующее окно, отображающее ошибку (полноэкранный диалог или снекбар соотв.)
 *
 * @param failure - ошибка, на основне которой открывается соответствующее окно ошибки.
 * @param getFailureInfo - функция, которая должна
 * вернуть соответсвующий [FailureInfo] указанному [Failure].
 * Для обработки доп. ошибок через данный параметр, необходимо создать функцию, в которой вернуть
 * объект [FailureInfo], содержащий сообщение ошибки и коллбек, производимый при получении
 * такой ошибки и нажатии кнопки "повторить/обновить".
 * */
fun Fragment.openFailureView(
    failure: Failure,
    getFailureInfo: (failure: Failure) -> FailureInfo? = { null }
) {
    // Открытие окна с ошибкой
    when (val failureInfo: FailureInfo? = getFailureInfo(failure)) {
        // Открываем полноэкранный диалог
        is FullScreenFailureInfo -> openFailureDialog(failureInfo)
        // Открываем снекбар
        is SmallFailureInfo -> openFailureSnackBar(failureInfo)
        // Если инфа об ошибке не указана (null), показываем базовую в полном экране
        null -> FullScreenFailureInfo(
            getString(R.string.error_base_title),
            getString(R.string.error_base)
        )
    }
}

// Открытие полноэкранного диалогового фрагмента с ошибкой
private fun Fragment.openFailureDialog(failureInfo: FullScreenFailureInfo) {
    // Открываем фрагмент, передавая инфу об ошибке
    val action = FailureFragmentDirections.actionGlobalFailureFragment(failureInfo)
    findNavController().navigate(action)
}

// Открытие снэкбара с возможностью повторить операцию
private fun Fragment.openFailureSnackBar(failureInfo: SmallFailureInfo) {
    makeSnackBar(
        failureInfo.text,
        failureInfo.buttonText ?: getString(R.string.retry),
        failureInfo.retryClickedCallback
    )
}