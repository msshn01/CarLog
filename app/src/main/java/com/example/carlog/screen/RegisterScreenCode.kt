package com.example.carlog.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carlog.R
import com.example.carlog.ui.theme.hintColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(){
    Scaffold(
        modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.background(color = Color.Black).fillMaxSize().padding(innerPadding)
            , horizontalAlignment = Alignment.CenterHorizontally
            , verticalArrangement = Arrangement.Center

        ) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var password2 by remember { mutableStateOf("") }

            //            TextField(value =itemName, onValueChange = { itemName = it }, placeholder = { Text("Enter name") })
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo")
            OutlinedTextField(
                value = email
                , onValueChange = { email = it }
                , modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
                , placeholder = { Text("Enter email", color = hintColor) }
                , shape = CircleShape,colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,   // Odaklanıldığında (yazı yazarken) metin rengi
                    unfocusedTextColor = Color.White, // Odak dışındayken metin rengi
                    cursorColor = Color.White        // İmleç (yazı imleci) rengi
                ))
            Spacer(modifier = Modifier.padding(15.dp))

            OutlinedTextField(
                value = password
                , onValueChange = { password = it }
                , modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
                , placeholder = { Text("Enter password", color = hintColor) }
                , shape = CircleShape,colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,   // Odaklanıldığında (yazı yazarken) metin rengi
                    unfocusedTextColor = Color.White, // Odak dışındayken metin rengi
                    cursorColor = Color.White        // İmleç (yazı imleci) rengi
                ))
            Spacer(modifier = Modifier.padding(18.dp))
            OutlinedTextField(
                value = password2
                , onValueChange = { password2 = it }
                , modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
                , placeholder = { Text("Enter password again", color = hintColor) }
                , shape = CircleShape,colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,   // Odaklanıldığında (yazı yazarken) metin rengi
                    unfocusedTextColor = Color.White, // Odak dışındayken metin rengi
                    cursorColor = Color.White        // İmleç (yazı imleci) rengi
                ))
            Spacer(modifier = Modifier.padding(18.dp))
            Button(
                onClick = { /* TODO: Login İşlemi */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .height(40.dp),                 // Dikeyde ideal tıklama kalınlığı (56dp)     // Sadece yanlardan 30dp boşluk bırakır
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xA6E82222), // Canlı yeşil accent rengi (veya koyu gri için 0xFF2C353E yapabilirsin)
                    contentColor = Color.White          // Yazı rengi
                ),
                shape = RoundedCornerShape(16.dp),      // Köşeleri yumuşatılmış kavisli tasarım
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,            // Şık bir derinlik/gölge hissi
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp                 // Harf aralığını biraz açarak premium görünüm sağlar
                )
            }
            Spacer(modifier = Modifier.padding(30.dp))





        }
    }
}


