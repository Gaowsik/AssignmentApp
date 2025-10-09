package com.example.assignmentapp.presentation.core

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.assignmentapp.R
import com.example.assignmentapp.presentation.extention.getProgressDialog
import com.example.assignmentapp.presentation.extention.showAlert
import com.example.assignmentapp.presentation.utils.getStringFromThisResourceId

open class BaseActivity : AppCompatActivity() {

    var mCurrentShowingAlert: AlertDialog? = null
    private var mProgressDialog: Dialog? = null
    private var mNetworkStatusRelatedAlertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        init()
    }

    private fun init() {
        initProgressDialog()
    }

    /**
     * network change state change broadcast listener
     */
    private val networkStatusBroadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.toUri(Intent.URI_INTENT_SCHEME)?.let {
                if (it.contains("B.noConnectivity=true"))
                    showAlertDialogForNetworkStatus()
                else {
                    dismissAlertDialogForNetworkStatus()
                }
            }
        }
    }

    private fun registerNetworkStatusBroadCast() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(networkStatusBroadCastReceiver, filter)
    }

    private fun unRegisterNetworkStatusBroadCast() {
        unregisterReceiver(networkStatusBroadCastReceiver)
    }

    private fun dismissAlertDialogForNetworkStatus() {
        mNetworkStatusRelatedAlertDialog?.dismiss()
    }

    private fun showAlertDialogForNetworkStatus() {
        if (!isFinishing) {
            mNetworkStatusRelatedAlertDialog?.dismiss()
            mNetworkStatusRelatedAlertDialog =
                showAlert(message = getString(R.string.msg_no_internet), isCancellable = false)
            mNetworkStatusRelatedAlertDialog?.show()
        }
    }

    private fun initProgressDialog() {
        mProgressDialog = getProgressDialog()
    }

    fun hideProgressDialog() {
        mProgressDialog?.dismiss()
    }

    fun showProgressDialog() {
        if (mProgressDialog?.isShowing == true)
            return
        initProgressDialog()
        mProgressDialog?.show()
    }

    fun showMessageDialogWithOkAction(
        messageContent: String?,
        okAction: (() -> Unit)? = null,
        isCancellable: Boolean = false,
        okActionText: String = getStringFromThisResourceId(R.string.action_ok, this),
    ) {
        configureAlertShowing(
            messageContent = messageContent,
            okAction = okAction,
            okActionText = okActionText,
            isCancellable = isCancellable,
            negativeAction = null,
            negativeActionText = null,
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
        configureAlertShowing(
            messageContent = messageContent,
            okAction = okAction,
            okActionText = okActionText,
            isCancellable = isCancellable,
            negativeAction = negativeAction,
            negativeActionText = negativeText,
        )
    }

    private fun configureAlertShowing(
        messageContent: String?,
        okAction: (() -> Unit)?,
        okActionText: String,
        isCancellable: Boolean,
        negativeAction: (() -> Unit)?,
        negativeActionText: String?,
    ) {
        messageContent?.let {
            mCurrentShowingAlert = showAlert(
                message = messageContent,
                okAction = okAction,
                okActionText = okActionText,
                isCancellable = isCancellable,
                negativeAction = negativeAction,
                negativeActionText = negativeActionText,
            )
        }
    }

    override fun onStart() {
        registerNetworkStatusBroadCast()
        super.onStart()
    }

    override fun onStop() {
        unRegisterNetworkStatusBroadCast()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        hideActionBar()
    }

    private fun hideActionBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }
}