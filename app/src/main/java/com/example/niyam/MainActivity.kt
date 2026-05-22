package com.example.niyam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.niyam.ui.bhajan.BhajanScreen
import com.example.niyam.ui.gita.GitaScreen
import com.example.niyam.ui.gita.GitaViewModel
import com.example.niyam.ui.home.HomeScreen
import com.example.niyam.ui.home.RoutineViewModel
import com.example.niyam.ui.meditation.MeditationScreen
import com.example.niyam.ui.theme.NiyamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NiyamTheme {
                NiyamNavHost()
            }
        }
    }
}

@Composable
fun NiyamNavHost() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val routineViewModel: RoutineViewModel = hiltViewModel()
            HomeScreen(
                onNavigateToMeditation = { navController.navigate("meditation") },
                onNavigateToGita = { navController.navigate("gita") },
                onNavigateToBhajan = { navController.navigate("bhajan") },
                routineViewModel = routineViewModel
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
    }
}
