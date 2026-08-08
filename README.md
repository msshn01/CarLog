# 🚗 AutoLogApp - Detaylı Proje Yol Haritası & Yapılacaklar Listesi

Uygulama; araç takibi, bakım/gider yönetimi, randevu sistemi, sigorta teklifleri ve AI asistan özelliklerini barındıran kapsamlı bir otomotiv asistanıdır.

---


Bu projede MVVM mimarisi kullanılarak Seperation of Concerns (Sorumlulukların Ayrılması) prensibi uygulanmıştır. Veri katmanı Repository Pattern ile soyutlanmış, UI tarafında Jetpack Compose ile reaktif bir yapı kurulmuştur.

## 🏗️ Faz 1: Temel Mimari, Yetkilendirme & Offline-First Yapı

### 1.1. Proje Altyapısı & Mimari
- [x] GitHub repository kurulumu ve yerel projeye bağlanması
- [x] Splash / Yükleme ekranı (SplashScreen) tasarımı ve animasyonları
- [x] Jetpack Navigation (NavBar / NavHost) rotalarının kurulması
- [x] MVVM + Clean Architecture temel paket yapısının (di, data, domain, ui) oluşturulması
- [ ] Dependency Injection (Hilt / Koin) entegrasyonu

### 1.2. Kimlik Doğrulama & Kullanıcı Yönetimi (Auth)
- [x] Firebase projesinin oluşturulması ve `google-services.json` eklenmesi
- [x] **Login Ekranı (Giriş Yap):**
    - [x] E-posta & Şifre alanları, validator kontrolleri (Email formatı, min 6 karakter)
    - [x] "Şifremi Unuttum" seçeneği ve sıfırlama e-postası gönderme akışı
    - [] Google ile Giriş Yap (Google Sign-In) buton entegrasyonu
- [x] **Register Ekranı (Kayıt Ol):**
    - [x] Ad-Soyad, E-posta, Şifre ve Şifre Tekrar form alanları
    - [ ] Kullanıcı sözleşmesi ve gizlilik politikası onay kutusu (Checkbox)
- [x] **Oturum Yönetimi:**
    - [x] Kullanıcı oturum durumunun (Beni Hatırla / Auto-Login) kontrolü

### 1.3. Yerel Veritabanı & Offline-First Senkronizasyon
- [ ] **Room Veritabanı:**
    - [ ] `VehicleEntity`, `MaintenanceEntity`, `ExpenseEntity` veritabanı tablolarının yazılması
    - [ ] DAO (Data Access Object) arayüzlerinin tanımlanması
- [ ] **Firestore Entegrasyonu:**
    - [ ] Bulut veritabanı koleksiyon yapısının (Users -> Vehicles -> Logs) kurgulanması
- [ ] **Arka Plan Senkronizasyonu (WorkManager):**
    - [ ] İnternet bağlantısını dinleyen `NetworkObserver` yazılması
    - [ ] Bağlantı geldiğinde yerel Room verilerini Firestore'a aktaran `SyncWorker` yazılması

---

## 🚘 Faz 2: Garaj & Araç Yönetimi (Vehicle Profile)

### 2.1. Araç Ekleme & Düzenleme (Add/Edit Vehicle)
- [x] **Form Ekranı UI:**
    - [x] Marka / Model seçimi
    - [x] Yıl, Güncel KM input alanları
    - [x] Muayene ve Sigorta tarih seçicileri (Date Picker)
- [x] Form doğrulama (KM alanı sayısal mı?, Boş alan kontrolü)

### 2.2. Garaj & Çoklu Araç Mantığı
- [x] Ana ekranda aktif araç kartlarının gösterimi (LazyRow / Carousel)
- [x] Aktif aracı değiştirme / Varsayılan araç seçme mantığı
- [x] Araç detay ekranı ve silme opsiyonu

---

## 🛠️ Faz 3: Bakım Takibi & Akıllı Bildirimler

### 3.1. Bakım Kaydı (Maintenance Logging)
- [x] **Bakım Ekleme Ekranı:**
    - [x] Yapılan işlem türü (Yağ/Filtre, Fren, Triger, Buji vb.)
    - [x] İşlem tarihi (Date Picker), yapıldığı KM, ve toplam ücret
- [x] Bakım geçmişi listeleme (Ana ekran üzerinde akıcı liste)

### 3.2. Akıllı Uyarı & Bildirim Sistemi
- [x] KM veya zaman bazlı kalan ömür hesaplama ve görselleştirme (Progress Bar)
- [x] **Local Push Notifications (Cihaz Bildirimleri):**
    - [x] Muayene ve Sigorta tarihi yaklaşınca periyodik bildirim gönderme (WorkManager)

---

## 💸 Faz 4: Gider Takibi & Finans Yönetimi

### 4.1. Gider Kaydı
- [ ] **Gider Ekleme Formu:**
    - [ ] Kategori seçimi (Akaryakıt, Yıkama, Otopark, Ceza, Aksesuar, Sigorta)
    - [ ] Tutar, tarih ve açıklama alanları
- [ ] Akaryakıt özel alanı: Litre fiyatı ve alınan litre hesabı

### 4.2. Raporlama & Grafikler
- [ ] Grafik kütüphanesi entegrasyonu (MPAndroidChart veya Vico)
- [ ] Aylık/Yıllık toplam harcama pasta grafiği (Pie Chart)
- [ ] KM başına ortalama yakıt/masraf maliyeti analizi

---

## 🤖 Faz 5: Yapay Zeka (AI) Chat Asistanı

### 5.1. Chat Arayüzü & Entegrasyon
- [ ] Chat ekranı UI tasarımı (Mesaj balonları, yazıyor... göstergesi)
- [ ] **Gemini API Entegrasyonu:**
    - [ ] API Key yapılandırması ve Güvenli depolama (`local.properties`)
    - [ ] Chat mesaj geçmişinin yerel veritabanında saklanması

### 5.2. Akıllı Prompt Mühendisliği
- [ ] **Context Injection (Kişiselleştirme):**
    - [ ] Aktif aracın marka, model, KM ve son bakım verilerinin AI'ya gizli prompt olarak iletilmesi
- [ ] Hızlı soru butonları *(Örn: "Motor arıza lambası neden yanar?", "100.000 KM bakımında ne değişir?")*

---

## 🛡️ Faz 6: Sigorta, Kasko & Sponsorlu Reklamlar

### 6.1. Poliçe Takibi
- [x] Sigorta & Kasko bitiş tarihleri takibi
- [x] Poliçe bitimine 4 ay kala başlayan aylık hatırlatıcılar

### 6.2. Monetization (Gelir Modülü)
- [ ] Google AdMob entegrasyonu (Banner & Geçiş reklamları)
- [ ] Sigorta firmaları / Acentalar için sponsorlu teklif banner alanları

---

## 📅 Faz 7: Usta Randevu & Servis Pazar Yeri

### 7.1. Servis Arama & Listeleme
- [ ] Google Maps / Konum API entegrasyonu
- [ ] Yakındaki özel servisleri ve ustaları haritada/listede gösterme
- [ ] Usta detay sayfası (Puanlama, yorumlar, uzmanlık alanları)

### 7.2. Randevu Sistemi
- [ ] Tarih ve saat seçerek randevu oluşturma formu
- [ ] Geçmiş ve yaklaşan randevularım ekranı