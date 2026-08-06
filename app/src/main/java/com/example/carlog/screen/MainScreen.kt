package com.example.carlog.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

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
    navController: NavController
) {
    var selectedCarId by remember { mutableStateOf("") }

    val carList by viewModel2.cars.collectAsState()
     // Varsayılan araç
    val isLoading by viewModel2.isLoading.collectAsState()
    val selectedCar = carList.firstOrNull { it.id == selectedCarId } ?: carList.firstOrNull() ?:
    Car(name = "Henüz araç eklenmedi"
        , km = "0"
        , maintenanceList = mutableListOf(
            Maintenance("Yapılan bakım","Bakım ayrıntıları burada gözükür")
        ))


    Scaffold(bottomBar = { SimpleBottomBar(navController) },
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
            selectedCar?.let { car ->
                Column(modifier = Modifier.fillMaxSize()) {
                    CarCardLazy(navController = navController, list = carList) { selected ->
                        selectedCarId = selected.id
                    }
                    CarSelectedDashboard(car, onAddMaintenanceClick = {
                        navController.navigate("addMaintenance/${car.id}")
                    })
                    DashboardQuickStatsSection()
                    InfoStatLazy(car.maintenanceList, modifier = Modifier.weight(1f))
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)), // Açık yeşil zemin
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        var showKmDialog by remember { mutableStateOf(false) }
        if (showKmDialog){
            UpdateKmDialog(onDismiss = { showKmDialog = false }){
                newKm ->
                // KM güncelleme işlemi burada yapılabilir
                viewModel.changeKm(carId =car.id, newKm = newKm)

            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ÜST SATIR: İkon + Araç Adı & KM Bilgisi
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sol Araç İkon Kutusu
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF2E7D32).copy(alpha = 0.15f),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Araç İsmi ve Kilometre Bilgisi
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = car.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "KM: ${car.km}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }

            // ALT SATIR: Butonlar (KM Güncelle & Bakım Ekle)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // KM Güncelle Butonu
                OutlinedButton(
                    onClick = {showKmDialog = true},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF2E7D32)
                    ),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "KM Güncelle", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                // Bakım Ekle Butonu
                Button(
                    onClick = onAddMaintenanceClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Bakım Ekle", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}


@Composable
fun AddLogFloatingActionButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF2E7D32), // Yeşil tonun
        contentColor = Color.White,
        shape = androidx.compose.foundation.shape.CircleShape // Tam yuvarlak form
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Bakım Ekle"
        )
    }
}

@Composable
fun CarCardLazy(
    // 1. Varsayılan parametreleri varsayılan değer olarak kalıba çakmak yerine dışarıdan da alabilmek en doğrusudur.
    list: List<Car>,
    modifier: Modifier = Modifier,
    navController: NavController,
    onCardClick: (Car) -> Unit
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
                    modifier = Modifier.width(220.dp)
                ){

                    onCardClick(item)
                }
            }
        }
    }
}


@Composable
fun Card(
    car: Car,
    modifier: Modifier = Modifier
    ,navController: NavController
    ,onCardClick: (String) -> Unit
) {
    Row(
        // Ekran genişliğini kaplaması ve elemanları iki uca yayması için:
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFA5D6A7)) // Hafif tatlı bir yeşil tonu (istediğin renkle değiştirebilirsin)
            .padding(16.dp)
            .clickable {
                onCardClick(car.name)
            }
            ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // HATA DÜZELTİLDİ: VerticalAlignment yerine Alignment.CenterVertically
    ) {
        // Sol taraftaki Metin Grubu
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = car.name,
                fontSize = 20.sp, // 30.sp başlık için biraz büyüktü, daha dengeli bir boyuta çekildi
                fontWeight = FontWeight.Bold,
                maxLines = 1, // Tek satıra sığdır
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF1B5E20)
            )
            Text(
                text = "${car.km} km",
                maxLines = 1, // Tek satıra sığdır
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp, // İkinci metni ikincil bilgi olduğu için küçülttük (Tipografi Hiyerarşisi)
                color = Color.DarkGray
            )
        }

        // Sağ taraftaki İkon
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = "Araç İkonu",
            tint = Color(0xFF1B5E20),
            modifier = Modifier.size(36.dp) // İkon boyutu netleştirildi
        )
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
    val darkMenuBg = Color(0xFF1E1E1E)      // Koyu gri menü arka planı
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
fun SimpleBottomBar(navController: NavController) {
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
                navController.navigate("logs") {

                }
            }
        )

        // 2. Ekran: Kayıtlar
        BottomNavItemSimple(
            icon = Icons.Default.InsertChart,
            label = "İstatistikler",
            onClick = {
                navController.navigate("statistika") {

                }
            }
        )

        // 3. Ekran: İstatistikler
        BottomNavItemSimple(
            icon = Icons.Default.Person,
            label = "Profil",
            onClick = {
                navController.navigate("profil") {

                }
            }
        )
    }
}

// Her bir butonun görünümü (İkon + Yazı)
@Composable
private fun BottomNavItemSimple(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF4CAF50) // Yeşil Vurgu Rengin
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun DashboardQuickStatsSection(
    modifier: Modifier = Modifier
) {
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
                value = "12 Mar 2024",
                subtitle = "(3.500 km önce)",
                icon = Icons.Default.Build, // Wrench ikonu
                modifier = Modifier.weight(1f) // Sol tarafı eşit kaplar
            )
            InfoStatCard(
                title = "Muayene",
                value = "180 Gün Kaldı",
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
                value = "Bitiş: 15 Haz 2024",
                subtitle = null,
                icon = Icons.Default.Shield, // Kalkan ikonu
                modifier = Modifier.weight(1f)
            )
            InfoStatCard(
                title = "Son Harcama",
                value = "1.200 ₺ (Yakıt)",
                subtitle = null,
                icon = Icons.Default.LocalGasStation, // Benzinlik ikonu
                modifier = Modifier.weight(1f)
            )
        }
    }
}








@Composable
fun InfoStatLazy(
    list: MutableList<Maintenance>,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        Text(
        text = "Araç bakım kayıtları",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Center, // Metni kendi genişliği içinde ortalar
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
    LazyColumn(modifier = Modifier.padding(7.dp)) {
        items(list) { item ->
            BottomCard(item.title, item.description, item.price, item.date)
            Spacer(Modifier.padding(5.dp))
        }
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
        modifier = modifier.height(100.dp).clip(RoundedCornerShape(26.dp)),
        color = Color(0xFFEFEFEF), // Görseldeki açık gri/lavanta tonu
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Başlık ve İkon Satırı
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
            }

            // Değer (Tarih / Gün / Tutar)
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            // Alt Detay (Varsa: Örn. 3.500 km önce)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(Modifier.size(20.dp))
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
        // Sabit height yerine padding vererek içeriğe göre yüksekliğinin esnemesini sağladık.
        // Kenar yuvarlatmasını (shape) doğrudan Surface parametresinde verdik.
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp), // 36.dp yerine daha dengeli 20.dp Material 3 kavis oranı
        color = Color(0xFFF5F5F5), // Şık, açık gri zemin
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. İkon Kutusu (Sol Taraf)
            Surface(
                shape = CircleShape,
                color = Color(0xFF2E7D32).copy(alpha = 0.12f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFF2E7D32)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 2. Sol-Orta Kısım: Başlık, Açıklama (value) ve Tarih
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // İşlem Başlığı (Örn: Yağ Değişimi)
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )

                // Ek Açıklama / Detay (Örn: Motul 5W-40)
                if (value.isNotBlank()) {
                    Text(
                        text = value,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }

                // Tarih (Daha küçük ve açık ton)
                Text(
                    text = date,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 3. Sağ Kısım: Tutar (money) Vurgusu
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE8F5E9) // Yeşilimsi tatlı bir vurgu kutusu
            ) {
                Text(
                    text = money,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}




