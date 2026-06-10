package com.example.niyam.ui.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.niyam.data.local.UserProfileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileManager: UserProfileManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val userName: StateFlow<String> = profileManager.userName
    val userHeight: StateFlow<Float> = profileManager.userHeight
    val userWeight: StateFlow<Float> = profileManager.userWeight
    val userGender: StateFlow<String> = profileManager.userGender
    val userPfpPath: StateFlow<String?> = profileManager.userPfpPath
    val isSetupComplete: StateFlow<Boolean> = profileManager.isSetupComplete

    fun saveProfile(
        name: String,
        heightStr: String,
        weightStr: String,
        gender: String,
        pfpUri: Uri?
    ): Boolean {
        if (name.isBlank() || heightStr.isBlank() || weightStr.isBlank() || gender.isBlank()) {
            return false
        }
        val height = heightStr.toFloatOrNull() ?: 0f
        val weight = weightStr.toFloatOrNull() ?: 0f
        
        var savedPfpPath = userPfpPath.value
        
        if (pfpUri != null) {
            try {
                val pfpFile = File(context.filesDir, "profile_pfp.jpg")
                context.contentResolver.openInputStream(pfpUri)?.use { input ->
                    FileOutputStream(pfpFile).use { output ->
                        input.copyTo(output)
                    }
                }
                savedPfpPath = pfpFile.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        profileManager.saveProfile(name, height, weight, gender, savedPfpPath)
        return true
    }
}
