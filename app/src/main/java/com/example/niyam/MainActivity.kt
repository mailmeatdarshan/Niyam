package com.example.niyam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.niyam.data.local.UserProfileManager
import com.example.niyam.ui.bhajan.BhajanScreen
import com.example.niyam.ui.gita.GitaScreen
import com.example.niyam.ui.gita.GitaViewModel
import com.example.niyam.ui.home.HomeScreen
import com.example.niyam.ui.home.RoutineViewModel
import com.example.niyam.ui.meditation.MeditationScreen
import com.example.niyam.ui.task.TaskScreen
import com.example.niyam.ui.task.TaskViewModel
import com.example.niyam.ui.water.WaterIntakeScreen
import com.example.niyam.ui.focus.FocusScreen
import com.example.niyam.ui.profile.ProfileSetupScreen
import com.example.niyam.ui.profile.ProfileViewModel
import com.example.niyam.ui.settings.SettingsScreen
import com.example.niyam.ui.about.AboutScreen
import com.example.niyam.ui.theme.NiyamTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userProfileManager: UserProfileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by userProfileManager.themeMode.collectAsState()
            
            NiyamTheme(themeMode = themeMode) {
                NiyamNavHost(userProfileManager = userProfileManager)
            }
        }
    }
}

@Composable
fun NiyamNavHost(userProfileManager: UserProfileManager) {
    val navController = rememberNavController()
    val isSetupComplete by userProfileManager.isSetupComplete.collectAsState()
    val userName by userProfileManager.userName.collectAsState()
    val userPfpPath by userProfileManager.userPfpPath.collectAsState()
    val themeMode by userProfileManager.themeMode.collectAsState()
    
    val startDestination = if (isSetupComplete) "home" else "profile_setup"
    
    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") {
            val routineViewModel: RoutineViewModel = hiltViewModel()
            HomeScreen(
                onNavigateToMeditation = { navController.navigate("meditation") },
                onNavigateToGita = { navController.navigate("gita") },
                onNavigateToBhajan = { navController.navigate("bhajan") },
                onNavigateToTasks = { navController.navigate("tasks") },
                onNavigateToWater = { navController.navigate("water") },
                onNavigateToFocus = { navController.navigate("focus") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToAbout = { navController.navigate("about") },
                userName = userName,
                userPfpPath = userPfpPath,
                routineViewModel = routineViewModel
            )
        }
        composable("profile_setup") {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileSetupScreen(
                viewModel = profileViewModel,
                isEditing = false,
                onSaveSuccess = {
                    navController.navigate("home") {
                        popUpTo("profile_setup") { inclusive = true }
                    }
                }
            )
        }
        composable("profile") {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileSetupScreen(
                viewModel = profileViewModel,
                isEditing = true,
                onSaveSuccess = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("settings") {
            SettingsScreen(
                currentTheme = themeMode,
                onThemeChanged = { userProfileManager.setThemeMode(it) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("about") {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("meditation") {
            MeditationScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("gita") {
            val viewModel: GitaViewModel = hiltViewModel()
            GitaScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("bhajan") {
            BhajanScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("tasks") {
            val taskViewModel: TaskViewModel = hiltViewModel()
            TaskScreen(
                viewModel = taskViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("water") {
            WaterIntakeScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("focus") {
            FocusScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
