package com.example.assignmentapp.data.source.local

import com.example.assignmentapp.data.Resource

interface PrefDataStore {
    suspend fun addBooleanToPref(key: String, value: Boolean)
    suspend fun addStringToPref(key: String, value: String): Resource<Unit>
    suspend fun getBooleanFromPref(key: String, defaultValue: Boolean): Boolean
    suspend fun getStringFromPref(key: String, defaultValue: String): String
    suspend fun clearDataStore(): Resource<Unit>
}