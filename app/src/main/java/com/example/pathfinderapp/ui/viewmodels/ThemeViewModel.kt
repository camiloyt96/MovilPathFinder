package com.example.pathfinderapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow(loadThemePreference())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private fun loadThemePreference(): Boolean {
        return prefs.getBoolean("is_dark_mode", false)
    }

    fun toggleTheme() {
        val newTheme = !_isDarkMode.value
        _isDarkMode.value = newTheme
        saveThemePreference(newTheme)
    }

    private fun saveThemePreference(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark_mode", isDark).apply()
    }
}}}
