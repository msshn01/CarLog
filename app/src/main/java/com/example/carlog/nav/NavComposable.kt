package com.example.carlog.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carlog.screen.AddCarScreen
import com.example.carlog.screen.LoginScreen
import com.example.carlog.screen.RegisterScreen
import com.example.carlog.screen.SplashScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import com.example.carlog.screen.MainScreen
import com.example.carlog.screen.ProfileScreen
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun NavBar(){

    var auth: FirebaseAuth = Firebase.auth
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "openScreen"){
        composable("home"){

            MainScreen(navController = navController)
        }
        composable("profil"){
            ProfileScreen(onLogout = {
                navController.navigate("loginScreen") {
                    popUpTo("home") { inclusive = true }
                }
            })
        }
        composable("logs"){
            com.example.carlog.screen.RecordsScreen()
        }
        composable("statistika"){
            com.example.carlog.screen.StatsScreen()
        }

        composable("addCar"){
            AddCarScreen(navController = navController)
        }

        composable("addMaintenance/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            com.example.carlog.screen.AddMaintenanceScreen(navController = navController, carId = carId)
        }

        composable("loginScreen"){
            LoginScreen(navController)
        }
        composable("registerScreen"){
            RegisterScreen(navController)
        }

        composable("openScreen"){
            SplashScreen(statusText = "Veriler yükleniyor...")

            LaunchedEffect(Unit) {

                delay(2000.milliseconds)


                val currentUser = auth.currentUser

                if (currentUser != null) {
                    navController.navigate("home") {
                        popUpTo("openScreen") { inclusive = true }
                    }
                } else {
                    navController.navigate("loginScreen") {
                        popUpTo("openScreen") { inclusive = true }
                    }
                }



            }
        }

    }
}