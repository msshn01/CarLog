package com.example.carlog.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.carlog.userInterface.auth.uiModel.UserViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.carlog.data.remote.CarDataViewModel
import com.example.carlog.model.Car
import com.example.carlog.model.Maintenance
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.*

/*
    viewModel.logOut()
    navController.navigate("loginScreen") {
    popUpTo("home") { inclusive = true }
    }
*/


@Composable
fun MainScreen(
    viewModel: UserViewModel = viewModel(),
    viewModel2: CarDataViewModel = viewModel(),
    navController: NavController,
    onNavigateToStats: () -> Unit = { navController.navigate("statistika") }
) {
    var selectedCarId by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var carToDelete by remember { mutableStateOf<Car?>(null) }

    val carList by viewModel2.cars.collectAsState()
     // Varsayılan araç
    val selectedCar = carList.firstOrNull { it.id == selectedCarId } ?: carList.firstOrNull() ?:
    Car(name = "Henüz araç eklenmedi"
        , km = "0"
        , maintenanceList = mutableListOf(
            Maintenance("Yapılan bakım","Bakım ayrıntıları burada gözükür")
        ))

    if (showDeleteDialog && carToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false 
                carToDelete = null
            },
            title = { Text("Aracı Sil") },
            text = { Text("${carToDelete?.name} isimli aracı silmek istediğinize emin misiniz?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        carToDelete?.let { car ->
                            viewModel2.deleteCar(car.id) {
                                showDeleteDialog = false
                                carToDelete = null
                            }
                        }
                    }
                ) {
                    Text("Sil", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDeleteDialog = false
                    carToDelete = null
                }) {
                    Text("İptal")
                }
            }
        )
    }

    Scaffold(bottomBar = { SimpleBottomBar(navController, onNavigateToStats) },
        floatingActionButton = {
            AddLogFloatingActionButton {
                navController.navigate("addCar")
        }},
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CarLogTopBar(
                viewModel,navController
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            selectedCar.let { car ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        CarCardLazy(
                            navController = navController,
                            list = carList,
                            onCardClick = { selected ->
                                selectedCarId = selected.id
                            },
                            onCardLongClick = { car ->
                                carToDelete = car
                                showDeleteDialog = true
                            }
                        )
                    }
                    item {
                        CarSelectedDashboard(car, onAddMaintenanceClick = {
                            navController.navigate("addMaintenance/${car.id}")
                        })
                    }
                    item {
                        DashboardQuickStatsSection(car = car)
                    }
                    item {
                        Text(
                            text = "Bakım Geçmişi",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    if (car.maintenanceList.isEmpty()) {
                        item {
                            Text(
                                text = "Henüz bir bakım kaydı bulunmuyor.",
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        items(car.maintenanceList.reversed()) { item ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                BottomCard(item.title, item.description, item.price, item.date)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CarSelectedDashboard(
   car : Car,
   onAddMaintenanceClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Dış çerçevenin genel ekran padding'i
    ) {
        // 1. Seçili Araç Kartı
        SelectedCarCard(car, onAddMaintenanceClick = onAddMaintenanceClick)

    }
}


@Composable
fun UpdateKmDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    // Giriş yapılan yeni KM değerini tutan yerel state
    var newKmText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        icon = {
            Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "Kilometre Güncelle",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            OutlinedTextField(
                value = newKmText,
                onValueChange = { newInput ->
                    if (newInput.isEmpty() || newInput.all { it.isDigit() }) {
                        newKmText = newInput
                    }
                },
                label = { Text("Yeni KM Değeri") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2E7D32),
                    focusedLabelColor = Color(0xFF2E7D32),
                    cursorColor = Color(0xFF2E7D32)
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(newKmText)
                    onDismiss()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White
                )
            ) {
                Text("Kaydet", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal", color = Color.Gray)
            }
        }
    )
}
@Composable
fun SelectedCarCard(
    car : Car,
    onAddMaintenanceClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CarDataViewModel = viewModel()
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth(),
    ) {
        var showKmDialog by remember { mutableStateOf(false) }
        if (showKmDialog){
            UpdateKmDialog(onDismiss = { showKmDialog = false }){
                newKm ->
                viewModel.changeKm(carId =car.id, newKm = newKm)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = car.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${car.km} Kilometre",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Durum İlerleme Çubukları (Muayene & Sigorta)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusProgressBar(
                    label = "Muayene Durumu",
                    dateStr = car.muayeneTarihi,
                    color = MaterialTheme.colorScheme.primary
                )
                StatusProgressBar(
                    label = "Sigorta Durumu",
                    dateStr = car.sigortaTarihi,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showKmDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "KM Güncelle", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onAddMaintenanceClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Bakım Ekle", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatusProgressBar(
    label: String,
    dateStr: String,
    color: Color
) {
    val progress = calculateDateProgress(dateStr)
    val daysLeft = calculateDaysRemaining(dateStr)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (daysLeft >= 0) "$daysLeft Gün Kaldı" else "Süresi Geçmiş",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (daysLeft > 30) color else MaterialTheme.colorScheme.error
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = if (daysLeft > 30) color else MaterialTheme.colorScheme.error,
            trackColor = color.copy(alpha = 0.1f),
        )
    }
}

private fun calculateDateProgress(dateStr: String): Float {
    return try {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val targetDate = sdf.parse(dateStr) ?: return 0f
        val today = Calendar.getInstance().time
        
        // Örnek: 1 yıllık bir periyot üzerinden ilerleme hesaplayalım (365 gün)
        val diff = targetDate.time - today.time
        val daysRemaining = (diff / (1000 * 60 * 60 * 24)).toFloat()
        
        // 365 günden başlayarak azalan bir progress (yaklaştıkça dolar)
        val p = (365f - daysRemaining) / 365f
        p.coerceIn(0f, 1f)
    } catch (e: Exception) {
        0f
    }
}

private fun calculateDaysRemaining(dateStr: String): Long {
    return try {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val targetDate = sdf.parse(dateStr) ?: return 0
        val today = Calendar.getInstance().time
        val diff = targetDate.time - today.time
        diff / (1000 * 60 * 60 * 24)
    } catch (e: Exception) {
        0
    }
}




@Composable
fun AddLogFloatingActionButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Araç Ekle"
        )
    }
}

@Composable
fun CarCardLazy(
    // 1. Varsayılan parametreleri varsayılan değer olarak kalıba çakmak yerine dışarıdan da alabilmek en doğrusudur.
    list: List<Car>,
    modifier: Modifier = Modifier,
    navController: NavController,
    onCardClick: (Car) -> Unit,
    onCardLongClick: (Car) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Araçlarım",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            // 2. Sadece start yerine üstten-alttan biraz nefes aldırmak daha şık durur
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // 3. Performans ve animasyonlar için 'key' parametresi ekledik
            items(
                items = list
            ) { item ->
                Card(car = item,
                    navController = navController,
                    modifier = Modifier.width(220.dp),
                    onLongClick = { onCardLongClick(item) }
                ){
                    onCardClick(item)
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Card(
    car: Car,
    modifier: Modifier = Modifier,
    navController: NavController,
    onLongClick: () -> Unit = {},
    onCardClick: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .combinedClickable(
                onClick = { onCardClick(car.name) },
                onLongClick = onLongClick
            ),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = car.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${car.km} km",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = "Araç İkonu",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarLogTopBar(
    viewModel: UserViewModel = viewModel(),
    navController: NavController
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    // Özel Renk Tanımlamaları
    val iconColor = Color(0xFFB0B0B0)       // İkon rengi
    val logoutRed = Color(0xFFFF5252)       // Çıkış butonu için kırmızı

    TopAppBar(
        title = {
            Text(
                text = "CarLog",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black // Üst bar tamamen siyah
        ),
        actions = {
            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menü",
                    tint = Color.White
                )
            }

            // ÖZELLEŞTİRİLMİŞ DROPDOWN MENU
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },

                // 1. Dış kılıfın varsayılan beyaz/gri rengini tamamen kaldırıyoruz
                containerColor = Color.Transparent,

                // 2. Kavisli şekli doğrudan şekil (shape) parametresine veriyoruz
                shape = RoundedCornerShape(16.dp),

                modifier = Modifier
                    // 3. Arka planı ve kavis içini burada renklendiriyoruz
                    .background(
                        color = Color.Black.copy(alpha = 0.85f), // Şeffaf siyah
                        shape = RoundedCornerShape(16.dp)
                    )
                    // Dilersen ince şık bir çerçeve (isteğe bağlı):
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                // 1. PROFIL SEÇENEĞİ
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Profilim",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profil",
                            tint = iconColor
                        )
                    },
                    onClick = {
                        isMenuExpanded = false
                        navController.navigate("profil")
                    }
                )

                // Araya İnce Çizgi (Ayraç)
                HorizontalDivider(color = Color(0xFF2C2C2C), thickness = 1.dp)

                // 2. ÇIKIŞ YAP SEÇENEĞİ (Kırmızı Vurgulu)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Çıkış Yap",
                            color = logoutRed, // Kırmızı yazı
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Çıkış Yap",
                            tint = logoutRed // Kırmızı ikon
                        )
                    },
                    onClick = {
                        isMenuExpanded = false
                        viewModel.logOut()
                        navController.navigate("loginScreen") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    )
}







@Composable
fun SimpleBottomBar(navController: NavController,onNavigateToStats: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E)) // Siyah/Koyu Teman
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Ekran: Araçlarım
        BottomNavItemSimple(

            icon = Icons.Default.TableChart,
            label =  "Kayıtlar",
            onClick = {
                navController.navigate("logs")
            }
        )

        // 2. Ekran: Kayıtlar
        BottomNavItemSimple(
            icon = Icons.Default.InsertChart,
            label = "İstatistikler",
            onClick = {
                onNavigateToStats()
            }
        )

        // 3. Ekran: İstatistikler
        BottomNavItemSimple(
            icon = Icons.Default.Person,
            label = "Profil",
            onClick = {
                navController.navigate("profil")
            }
        )
    }
}

@Composable
private fun BottomNavItemSimple(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DashboardQuickStatsSection(
    car: Car,
    modifier: Modifier = Modifier
) {
    val lastMaintenance = car.maintenanceList.lastOrNull()

    // 1 Dış Column (İki satırı üst üste tutar)
    Column(
        modifier = modifier.fillMaxWidth().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Satırlar arası dikey boşluk
    ) {
        // --- 1. SATIR: Son Bakım & Muayene ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Kartlar arası yatay boşluk
        ) {
            InfoStatCard(
                title = "Son Bakım",
                value = lastMaintenance?.title ?: "Kayıt Yok",
                subtitle = lastMaintenance?.date,
                icon = Icons.Default.Build, // Wrench ikonu
                modifier = Modifier.weight(1f) // Sol tarafı eşit kaplar
            )
            InfoStatCard(
                title = "Muayene",
                value = car.muayeneTarihi.ifBlank { "Belirtilmedi" },
                subtitle = null,
                icon = Icons.Default.Event, // Takvim ikonu
                modifier = Modifier.weight(1f) // Sağ tarafı eşit kaplar
            )
        }

        // --- 2. SATIR: Sigorta & Son Harcama ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoStatCard(
                title = "Sigorta",
                value = car.sigortaTarihi.ifBlank { "Belirtilmedi" },
                subtitle = null,
                icon = Icons.Default.Shield, // Kalkan ikonu
                modifier = Modifier.weight(1f)
            )
            InfoStatCard(
                title = "Son Harcama",
                value = lastMaintenance?.price ?: "0 ₺",
                subtitle = null,
                icon = Icons.Default.MonetizationOn,
                modifier = Modifier.weight(1f)
            )
        }
    }
}








@Composable
fun InfoStatCard(
    title: String,
    value: String,
    subtitle: String?,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}





@Composable
fun BottomCard(
    title: String,
    value: String,
    money: String,
    date: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (value.isNotBlank()) {
                    Text(
                        text = value,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = date,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = money,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}




