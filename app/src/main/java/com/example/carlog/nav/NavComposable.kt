package com.example.carlog.nav

import androidx.compose.foundation.layout.padding
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
import com.example.carlog.screen.LoginScreen
import com.example.carlog.screen.RegisterScreen
import com.example.carlog.screen.SplashScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import com.example.carlog.screen.MainScreen

@Composable
fun NavBar(){

    var auth: FirebaseAuth = Firebase.auth
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "openScreen"){
        composable("home"){
            MainScreen(navController = navController)
        }
        composable("profil"){

        }
        composable("loginScreen"){
            LoginScreen(navController)
        }
        composable("registerScreen"){
            RegisterScreen(navController)
        }
        composable("detailCarScreen"){
            Text(text = "DetailCarScreen", fontSize = 50.sp, modifier = Modifier.padding(40.dp))
        }
        composable("openScreen"){
            SplashScreen(statusText = "Veriler yükleniyor...")

            LaunchedEffect(Unit) {

                delay(2000)


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