package com.kode.contacts.presentation.base.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.kode.contacts.R
import com.kode.contacts.presentation.base.entity.Event
import com.kode.contacts.presentation.base.entity.UiState
import com.kode.contacts.presentation.base.exception.FailureFragmentDirections
import com.kode.contacts.presentation.base.exception.FailureInfo
import com.kode.domain.base.exception.Failure

fun Fragment.makeAlertDialog(
    @StringRes title: Int? = null,
    @StringRes message: Int,
    positiveCallback: () -> Unit,
    negativeCallback: () -> Unit,
    @StringRes positiveText: Int = R.string.ok,
    @StringRes negativeText: Int? = null,
) {
    makeAlertDialog(
        title = title?.let { getString(it) },
        message = getString(message),
        positiveText = getString(positiveText),
        positiveCallback = positiveCallback,
        negativeText = negativeText?.let { getString(negativeText) },
        negativeCallback = negativeCallback
    )
}

fun Fragment.makeAlertDialog(
    title: String? = null,
    message: String,
    positiveCallback: () -> Unit,
    negativeCallback: (() -> Unit)? = null,
    positiveText: String = getString(R.string.ok),
    negativeText: String? = null,
) {
    val baseAlertDialog = AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { _, _ -> positiveCallback() }
    // ???????????????????? ?????????????? ?? ?????????? ??????????????????????
    if (negativeCallback != null && negativeText != null) {
        baseAlertDialog.setNegativeButton(negativeText) { _, _ -> negativeCallback() }
    }
    baseAlertDialog.show()
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
 * ?????????????????????? ???? ?????????????????????????? ???????????? getContentIfNotHandled ?????? [LiveData] ?? ?????????? [Event]:
 * @param [observer] ????????????????????, ???????????? ???????? ?????????? ?????? ???? ??????????????????.
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
 * ???????????????????? ???? [UiState.Failure].
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
 * ?????????????? ?????????????????? ???????? ???????????? ?? ?????????????????????? ???? ?????????????????????? ?????????????????? [failure].
 * ???????? ???????????????????? ???????????????? [failure]
 * ???? ??????????????????, ???????????????????? ?????????????????????? ?????????????????? ????????????.
 *
 * ?? ?????????????????????? ???? ?????????????????????????? ???????? ([FailureInfo.FullScreen] ?????? [FailureInfo.Small])
 * ?????????????????????? ?????????????????????????????? ????????, ???????????????????????? ???????????? (?????????????????????????? ???????????? ?????? ?????????????? ??????????.)
 *
 * @param failure - ????????????, ???? ?????????????? ?????????????? ?????????????????????? ?????????????????????????????? ???????? ????????????.
 * @param getFailureInfo - ??????????????, ?????????????? ????????????
 * ?????????????? ???????????????????????????? [FailureInfo] ???????????????????? [Failure].
 * ?????? ?????????????????? ??????. ???????????? ?????????? ???????????? ????????????????, ???????????????????? ?????????????? ??????????????, ?? ?????????????? ??????????????
 * ???????????? [FailureInfo], ???????????????????? ?????????????????? ???????????? ?? ??????????????, ???????????????????????? ?????? ??????????????????
 * ?????????? ???????????? ?? ?????????????? ???????????? "??????????????????/????????????????".
 * */
fun Fragment.openFailureView(
    failure: Failure,
    getFailureInfo: (failure: Failure) -> FailureInfo? = { null }
) {
    // ???????????????? ???????? ?? ??????????????
    when (val failureInfo: FailureInfo? = getFailureInfo(failure)) {
        // ?????????????????? ?????????????????????????? ????????????
        is FailureInfo.FullScreen -> openFailureDialog(failureInfo)
        // ?????????????????? ??????????????
        is FailureInfo.Small -> openFailureSnackBar(failureInfo)
        // ???????? ???????? ???? ???????????? ???? ?????????????? (null), ???????????????????? ?????????????? ?? ???????????? ????????????
        null -> openFailureSnackBar(FailureInfo.Small({}, getString(R.string.error_base_title)))
    }
}

// ???????????????? ???????????????????????????? ?????????????????????? ?????????????????? ?? ??????????????
private fun Fragment.openFailureDialog(failureInfo: FailureInfo.FullScreen) {
    // ?????????????????? ????????????????, ?????????????????? ???????? ???? ????????????
    val action = FailureFragmentDirections.actionGlobalFailureFragment(failureInfo)
    findNavController().navigate(action)
}

// ???????????????? ???????????????? ?? ???????????????????????? ?????????????????? ????????????????
private fun Fragment.openFailureSnackBar(failureInfo: FailureInfo.Small) {
    makeSnackBar(
        failureInfo.text,
        failureInfo.buttonText ?: getString(R.string.retry),
        failureInfo.retryClickedCallback
    )
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.setupToolbarWithNavController(toolbar: Toolbar) {
    findNavController().apply {
        val appBarConfiguration = AppBarConfiguration(graph)
        toolbar.setupWithNavController(this, appBarConfiguration)
    }
}

fun Fragment.setupToolbarAndDrawerWithNavController(
    toolbar: Toolbar,
    drawerLayout: DrawerLayout,
    navigationView: NavigationView
) {
    findNavController().apply {
        val appBarConfiguration = AppBarConfiguration(graph, drawerLayout)
        toolbar.setupWithNavController(this, appBarConfiguration)
        navigationView.setupWithNavController(this)
    }
}