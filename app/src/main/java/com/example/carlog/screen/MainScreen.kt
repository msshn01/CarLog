package com.example.carlog.screen

import android.widget.Button
import android.widget.Toast
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCarFilled
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOffCsred
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.carlog.data.remote.CarDataViewModel
import com.example.carlog.model.Car

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
    var carName by remember { mutableStateOf("Seçilmedi") }
    var carKm by remember { mutableStateOf("Seçilmedi") }
    val carList by viewModel2.cars.collectAsState()
    val isLoading by viewModel2.isLoading.collectAsState()
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
        Column(
            modifier = Modifier.padding(innerPadding)) {

            CarCardLazy(navController = navController, list = carList){
                string, string1 ->
                carName = string
                carKm = string1
            }
            CarSelectedDashboard(carName,carKm)
            DashboardQuickStatsSection()
            InfoStatLazy(liste = listOf("","","","",""))
        }
    }
}


@Composable
fun CarSelectedDashboard(
    name: String,
    km : String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Dış çerçevenin genel ekran padding'i
    ) {
        // 1. Seçili Araç Kartı
        SelectedCarCard(name,km)

    }
}


@Composable
fun SelectedCarCard(
    name : String,
    km : String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        modifier = Modifier.fillMaxWidth().height(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sol Araç İkonu
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Sağ Metinler ve Buton Alanı
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // KM Yazısı ve Buton Yan Yana!
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Km: $km",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )


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
    onCardClick: (String, String) -> Unit
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
                Card(navController = navController,
                    name = item.name,
                    km = "Km ${item.km}",
                    // 4. Kartların ekranda düzgün, eşit ve sabit bir genişlikte durması için width veriyoruz:
                    modifier = Modifier.width(220.dp)
                ){

                    onCardClick(item.name,item.km)
                }
            }
        }
    }
}


@Composable
fun Card(
    name: String
    , km: String
    , modifier: Modifier = Modifier
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
                onCardClick(name)
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
                text = name,
                fontSize = 20.sp, // 30.sp başlık için biraz büyüktü, daha dengeli bir boyuta çekildi
                fontWeight = FontWeight.Bold,
                maxLines = 1, // Tek satıra sığdır
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF1B5E20)
            )
            Text(
                text = "$km km",
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
    liste : List<String>
){
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
        items(liste) {
            item ->

            BottomCard("Yağ bakımı",  "Yağ castrol ile değişti","1.200 ₺","12 Mar 2024")
            Spacer(Modifier.padding(5.dp))
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
    BottomCard("Yağ bakımı","Yağ castrol ile değişti","1.200 ₺","12 Mar 2024")
}




