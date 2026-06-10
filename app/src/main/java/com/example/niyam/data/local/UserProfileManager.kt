package com.example.niyam.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("niyam_profile_prefs", Context.MODE_PRIVATE)

    private val _userName = MutableStateFlow(prefs.getString("user_name", "") ?: "")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userHeight = MutableStateFlow(prefs.getFloat("user_height", 0f))
    val userHeight: StateFlow<Float> = _userHeight.asStateFlow()

    private val _userWeight = MutableStateFlow(prefs.getFloat("user_weight", 0f))
    val userWeight: StateFlow<Float> = _userWeight.asStateFlow()

    private val _userGender = MutableStateFlow(prefs.getString("user_gender", "") ?: "")
    val userGender: StateFlow<String> = _userGender.asStateFlow()

    private val _userPfpPath = MutableStateFlow(prefs.getString("user_pfp_path", null))
    val userPfpPath: StateFlow<String?> = _userPfpPath.asStateFlow()

    private val _isSetupComplete = MutableStateFlow(prefs.getBoolean("is_setup_complete", false))
    val isSetupComplete: StateFlow<Boolean> = _isSetupComplete.asStateFlow()

    private val _themeMode = MutableStateFlow(prefs.getString("theme_mode", "dark") ?: "dark")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    fun saveProfile(
        name: String,
        height: Float,
        weight: Float,
        gender: String,
        pfpPath: String?
    ) {
        prefs.edit().apply {
            putString("user_name", name)
            putFloat("user_height", height)
            putFloat("user_weight", weight)
            putString("user_gender", gender)
            putString("user_pfp_path", pfpPath)
            putBoolean("is_setup_complete", true)
            apply()
        }
        _userName.value = name
        _userHeight.value = height
        _userWeight.value = weight
        _userGender.value = gender
        _userPfpPath.value = pfpPath
        _isSetupComplete.value = true
    }

    fun setThemeMode(mode: String) {
        prefs.edit().putString("theme_mode", mode).apply()
        _themeMode.value = mode
    }

    fun clearProfile() {
        prefs.edit().clear().apply()
        _userName.value = ""
        _userHeight.value = 0f
        _userWeight.value = 0f
        _userGender.value = ""
        _userPfpPath.value = null
        _isSetupComplete.value = false
        _themeMode.value = "dark"
    }
}
