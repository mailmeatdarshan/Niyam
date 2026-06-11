package com.example.niyam.ui.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niyam.data.local.CompletionDao
import com.example.niyam.data.local.CompletionRecord
import com.example.niyam.data.local.UserProfileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileManager: UserProfileManager,
    private val completionDao: CompletionDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val userName: StateFlow<String> = profileManager.userName
    val userHeight: StateFlow<Float> = profileManager.userHeight
    val userWeight: StateFlow<Float> = profileManager.userWeight
    val userGender: StateFlow<String> = profileManager.userGender
    val userPfpPath: StateFlow<String?> = profileManager.userPfpPath
    val isSetupComplete: StateFlow<Boolean> = profileManager.isSetupComplete

    val allCompletionRecords: StateFlow<List<CompletionRecord>> = completionDao.getAllRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalCompletions: StateFlow<Int> = allCompletionRecords
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val currentStreak: StateFlow<Int> = allCompletionRecords
        .map { records ->
            calculateStreak(records.map { it.completedAt })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

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

    private fun calculateStreak(completionTimes: List<Long>): Int {
        if (completionTimes.isEmpty()) return 0
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val completedDays = completionTimes.map { sdf.format(Date(it)) }.distinct().sortedDescending()
        
        val todayStr = sdf.format(Date())
        val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterdayStr = sdf.format(yesterdayCal.time)
        
        // If last completion isn't today or yesterday, streak is broken
        if (completedDays.first() != todayStr && completedDays.first() != yesterdayStr) {
            return 0
        }
        
        var streak = 0
        val checkCal = Calendar.getInstance()
        
        // If today is completed, check starting today; else start from yesterday
        if (completedDays.first() != todayStr) {
            checkCal.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        while (true) {
            val dateStr = sdf.format(checkCal.time)
            if (completedDays.contains(dateStr)) {
                streak++
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        return streak
    }
}
