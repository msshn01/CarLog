package com.example.carlog.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsCarFilled
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carlog.data.remote.CarDataViewModel
import com.example.carlog.model.Car


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(
    navController: NavController,
    viewModel: CarDataViewModel = viewModel()
) {
    val context = LocalContext.current

    // State'lerin ViewModel üzerinden dinlenmesi
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Form Field State'leri
    var carName by remember { mutableStateOf("") }
    var carModel by remember { mutableStateOf("") }
    var carYear by remember { mutableStateOf("") }
    var carKm by remember { mutableStateOf("") }

    // Hata mesajı varsa Toast gösterip state'i temizleme
    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Yeni Araç Ekle",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Marka / İsim
            CarInputField(
                value = carName,
                onValueChange = { carName = it },
                label = "Marka / Araç Adı",
                placeholder = "Örn: Honda",
                leadingIcon = Icons.Default.DirectionsCar
            )

            // 2. Model
            CarInputField(
                value = carModel,
                onValueChange = { carModel = it },
                label = "Model / Paket",
                placeholder = "Örn: Civic VTEC II",
                leadingIcon = Icons.Default.DirectionsCarFilled
            )

            // 3. Yıl ve KM (Yan Yana)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CarInputField(
                        value = carYear,
                        onValueChange = { if (it.length <= 4) carYear = it },
                        label = "Model Yılı",
                        placeholder = "2005",
                        leadingIcon = Icons.Default.CalendarToday,
                        keyboardType = KeyboardType.Number
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    CarInputField(
                        value = carKm,
                        onValueChange = { carKm = it },
                        label = "Güncel KM",
                        placeholder = "286000",
                        leadingIcon = Icons.Default.Speed,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Kaydet Butonu
            Button(
                onClick = {
                    if (carName.isNotBlank()) {
                        val newCar = Car(
                            name = carName,
                            model = carModel.ifBlank { null },
                            year = carYear.ifBlank { null },
                            km = carKm.ifBlank { "0" },
                            maintenanceList = mutableListOf()
                        )

                        // ViewModel üzerinden kaydetme tetiklenir
                        viewModel.saveCar(newCar) {
                            Toast.makeText(context, "${newCar.name} garaja eklendi!", Toast.LENGTH_SHORT).show()
                            // Kayıt başarılı olduğunda ana ekrana döner
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                ),
                enabled = !isLoading && carName.isNotBlank() // Yüklenirken veya isim boşken tıklanamaz
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Garaja Kaydet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Reusable Custom TextField Component
@Composable
private fun CarInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Color.Gray) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF2E7D32)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2E7D32),
            focusedLabelColor = Color(0xFF2E7D32),
            unfocusedBorderColor = Color(0xFFE0E0E0)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
