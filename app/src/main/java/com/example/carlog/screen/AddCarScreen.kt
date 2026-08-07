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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsCarFilled
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.text.SimpleDateFormat
import java.util.*


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
    var carMuayene by remember { mutableStateOf("") }
    var carSigorta by remember { mutableStateOf("") }

    // Date Picker States
    var showMuayenePicker by remember { mutableStateOf(false) }
    var showSigortaPicker by remember { mutableStateOf(false) }

    // Hata mesajı varsa Toast gösterip state'i temizleme
    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    // Date Picker Dialogs
    if (showMuayenePicker) {
        DatePickerModal(
            onDateSelected = { 
                carMuayene = it
                showMuayenePicker = false 
            },
            onDismiss = { showMuayenePicker = false }
        )
    }

    if (showSigortaPicker) {
        DatePickerModal(
            onDateSelected = { 
                carSigorta = it
                showSigortaPicker = false 
            },
            onDismiss = { showSigortaPicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Yeni Araç Ekle",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

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
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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

            // 4. Muayene ve Sigorta (Yan Yana) - Artık tıklanabilir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CarInputField(
                        value = carMuayene,
                        onValueChange = { carMuayene = it },
                        label = "Muayene Bitiş",
                        placeholder = "Seçiniz",
                        leadingIcon = Icons.Default.Event,
                        readOnly = true,
                        onClick = { showMuayenePicker = true }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    CarInputField(
                        value = carSigorta,
                        onValueChange = { carSigorta = it },
                        label = "Sigorta Bitiş",
                        placeholder = "Seçiniz",
                        leadingIcon = Icons.Default.Shield,
                        readOnly = true,
                        onClick = { showSigortaPicker = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Kaydet Butonu
            Button(
                onClick = {
                    if (carName.isNotBlank()) {
                        val newCar = Car(
                            name = carName,
                            model = carModel.ifBlank { null },
                            year = carYear.ifBlank { null },
                            km = carKm.ifBlank { "0" },
                            muayeneTarihi = carMuayene,
                            sigortaTarihi = carSigorta,
                            maintenanceList = mutableListOf()
                        )

                        viewModel.saveCar(newCar) {
                            Toast.makeText(context, "${newCar.name} garaja eklendi!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                enabled = !isLoading && carName.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        text = "Garaja Kaydet",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        sdf.format(Date(it))
    } ?: ""

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                if (selectedDate.isNotEmpty()) {
                    onDateSelected(selectedDate)
                }
                onDismiss()
            }) {
                Text("Seç")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    ) {
        DatePicker(state = datePickerState)
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
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    onClick: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontWeight = FontWeight.Medium) },
        placeholder = { Text(placeholder, color = Color.Gray) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        singleLine = true,
        readOnly = readOnly,
        enabled = true,
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is androidx.compose.foundation.interaction.PressInteraction.Release) {
                            if (readOnly) onClick()
                        }
                    }
                }
            },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = if (readOnly) Color(0xFFF5F5F5) else Color.Transparent,
            unfocusedContainerColor = if (readOnly) Color(0xFFF5F5F5) else Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

