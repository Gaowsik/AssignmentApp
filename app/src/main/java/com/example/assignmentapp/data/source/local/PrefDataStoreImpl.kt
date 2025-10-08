package com.example.assignmentapp.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.assignmentapp.data.BaseRepo
import com.example.assignmentapp.data.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "assignment_data")

class PrefDataStoreImpl @Inject constructor(@ApplicationContext context: Context) : PrefDataStore,
    BaseRepo() {
    private val dataStore = context.dataStore
    override suspend fun addBooleanToPref(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    override suspend fun addStringToPref(key: String, value: String): Resource<Unit> = safeCall {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun clearDataStore() = safeCall {
        dataStore.edit { it.clear() }
        Unit
    }
}