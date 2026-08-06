package com.example.carlog.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carlog.data.remote.CarDataViewModel
import com.example.carlog.model.Car
import com.example.carlog.model.Maintenance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    viewModel: CarDataViewModel = viewModel()
) {
    val carList by viewModel.cars.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Tüm araçların bakımlarını tek bir listede toplayalım ve araç bilgisiyle eşleştirelim
    val allRecords = remember(carList, searchQuery) {
        carList.flatMap { car ->
            car.maintenanceList.map { it to car }
        }.filter { (maintenance, car) ->
            maintenance.title.contains(searchQuery, ignoreCase = true) ||
            car.name.contains(searchQuery, ignoreCase = true)
        }.sortedByDescending { it.first.date } // Tarihe göre sıralama (varsayım: tarih formatı uygunsa)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tüm Kayıtlar", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Arama Çubuğu
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("İşlem veya araç ara...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2E7D32),
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (allRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (searchQuery.isEmpty()) "Henüz kayıt bulunmuyor." else "Arama sonucu bulunamadı.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allRecords) { (maintenance, car) ->
                        RecordItemCard(maintenance, car)
                    }
                }
            }
        }
    }
}

@Composable
fun RecordItemCard(maintenance: Maintenance, car: Car) {
    // MainScreen'deki BottomCard tasarımına benzer ama araç ismini de gösteren bir yapı
    BottomCard(
        title = maintenance.title,
        value = "${car.name} - ${maintenance.description}",
        money = maintenance.price,
        date = maintenance.date
    )
}

@Preview(showBackground = true)
@Composable
fun RecordsPreview() {
    RecordsScreen()
}
