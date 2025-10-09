package com.example.assignmentapp.presentation.utils

import android.content.Context
import androidx.annotation.StringRes

fun getStringFromThisResourceId(@StringRes intResId: Int, context: Context): String {
    return context.getString(intResId)
}