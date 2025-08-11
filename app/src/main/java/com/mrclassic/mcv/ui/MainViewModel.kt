package com.mrclassic.mcv.ui

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Application.dataStore by preferencesDataStore(name = "settings")

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val FIRST_RUN_KEY = booleanPreferencesKey("first_run_shown")

    fun shouldShowFirstRunDialog(): Flow<Boolean> =
        getApplication<Application>().dataStore.data.map { prefs ->
            !prefs[FIRST_RUN_KEY].orDefault(false)
        }

    suspend fun setFirstRunShown() {
        getApplication<Application>().dataStore.edit { prefs ->
            prefs[FIRST_RUN_KEY] = true
        }
    }

    private fun Boolean?.orDefault(default: Boolean) = this ?: default
}