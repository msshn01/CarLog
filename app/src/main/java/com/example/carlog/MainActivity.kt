package com.example.carlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.carlog.navigation.NavBar
import com.example.carlog.screen.AppBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavBar()
        }
    }













    @Composable
    fun MainScreen(){
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = { AppBar() }) {
            innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {

    }
}


