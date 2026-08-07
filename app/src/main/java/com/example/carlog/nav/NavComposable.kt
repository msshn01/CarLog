package com.example.carlog.nav

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carlog.AdManager // AdManager sınıfının import edildiğinden emin ol
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

    // 1. REKLAM YÖNETİMİ KURULUMU
    val context = LocalContext.current
    val activity = context as? Activity
    val adManager = remember { AdManager(context) }

    // Google Test Interstitial Ad Unit ID
    val TEST_AD_UNIT = "ca-app-pub-3940256099942544/1033173712"
    //ca-app-pub-9023363539921560/8161955804

    // NavBar ilklendiğinde reklamı arka planda indirmeye başlatıyoruz
    LaunchedEffect(Unit) {
        adManager.loadInterstitialAd(TEST_AD_UNIT)
    }

    NavHost(navController = navController, startDestination = "openScreen"){
        composable("home"){
            // MainScreen'e istatistik ekranına gitme tetikleyicisini veya navController'ı geçebilirsin
            MainScreen(
                navController = navController,
                onNavigateToStats = {
                    // Home -> Statistika geçişinde reklamı gösteriyoruz
                    if (activity != null) {
                        adManager.showInterstitialAd(activity) {
                            // Reklam kapandığında veya yüklenemediyse statistika ekranına git
                            navController.navigate("statistika")

                            // Bir sonraki geçiş için reklamı tekrar yükle
                            adManager.loadInterstitialAd(TEST_AD_UNIT)
                        }
                    } else {
                        navController.navigate("statistika")
                    }
                }
            )
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