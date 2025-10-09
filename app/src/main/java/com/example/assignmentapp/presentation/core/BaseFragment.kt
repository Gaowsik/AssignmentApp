package com.example.assignmentapp.presentation.core

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    private var mBaseActivity: BaseActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mBaseActivity = context as BaseActivity
    }

    fun showMessageDialogWithOkAction(
        messageContent: String?,
        isCancellable: Boolean = false,
        okAction: (() -> Unit)? = null
    ) {
        mBaseActivity?.showMessageDialogWithOkAction(
            messageContent = messageContent,
            okAction = okAction,
            isCancellable = isCancellable
        )
    }

    fun showMessageDialogWithPositiveAndNegativeAction(
        messageContent: String?,
        okAction: (() -> Unit)? = null,
        okActionText: String,
        isCancellable: Boolean = false,
        negativeAction: (() -> Unit)? = null,
        negativeText: String?,
    ) {
        mBaseActivity?.showMessageDialogWithPositiveAndNegativeAction(
            messageContent = messageContent,
            okAction = okAction,
            isCancellable = isCancellable,
            okActionText = okActionText,
            negativeAction = negativeAction,
            negativeText = negativeText
        )
    }


    /**
     * show Progress Dialog
     */
    fun showProgressDialog() {
        mBaseActivity?.showProgressDialog()
    }


    /**
     * hide Progress dialog
     */
    fun hideProgressDialog() {
        mBaseActivity?.hideProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        hideActionBar()
    }

    private fun hideActionBar() {
        mBaseActivity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        mBaseActivity?.actionBar?.hide()
    }

}