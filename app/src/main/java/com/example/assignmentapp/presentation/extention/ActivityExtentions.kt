package com.example.assignmentapp.presentation.extention

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assignmentapp.R
import com.example.assignmentapp.presentation.utils.AlertDialogHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private fun Activity.alert(
    title: CharSequence? = null,
    message: CharSequence? = null,
    isCancellable: Boolean = true,
    func: AlertDialogHelper.() -> Unit
): AlertDialog {
    return AlertDialogHelper(this, title, message).apply {
        cancelable = isCancellable
        func()
    }.create().also {
        it
    }
}


/**
 * show alert with less code base
 */
fun AppCompatActivity.showAlert(
    message: String,
    isCancellable: Boolean = true,
    title: String = getString(R.string.title_alert),
    okActionText: String = getString(R.string.action_ok),
    okAction: (() -> Unit)? = null,
    negativeAction: (() -> Unit)? = null,
    negativeActionText: String? = null
): AlertDialog {
    return alert(title, message) {
        cancelable = isCancellable
        positiveButton(okActionText) {
            okAction?.invoke()
        }
        negativeAction?.let {
            negativeButton(negativeActionText ?: getString(R.string.action_cancel)) {
                negativeAction.invoke()
            }
        }
    }.also {
        if (it.isShowing)
            it.dismiss()
        it.show()
    }
}

fun <T> AppCompatActivity.collectLatestLifeCycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

inline fun <reified T : Activity> Context?.startActivity(func: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java).apply {
        func()
    }
    this?.startActivity(intent)
}