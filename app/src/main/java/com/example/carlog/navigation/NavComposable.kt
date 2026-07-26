package com.example.carlog.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carlog.screen.LoginScreen
import com.example.carlog.screen.RegisterScreen
import com.example.carlog.screen.SplashScreen
import kotlinx.coroutines.delay


@Composable
fun NavBar(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "openScreen"){
        composable("home"){

        }
        composable("profil"){

        }
        composable("loginScreen"){
            LoginScreen(navController)
        }
        composable("registerScreen"){
            RegisterScreen()
        }
        composable("openScreen"){
            SplashScreen(statusText = "Veriler yükleniyor...")

            LaunchedEffect(Unit) {

                delay(2000)


                navController.navigate("loginScreen") {

                    popUpTo("openScreen") { inclusive = true }
                }
            }
        }

    }
}