# 🚗 AutoLogApp - Detaylı Proje Yol Haritası & Yapılacaklar Listesi

Uygulama; araç takibi, bakım/gider yönetimi, randevu sistemi, sigorta teklifleri ve AI asistan özelliklerini barındıran kapsamlı bir otomotiv asistanıdır.

---

## 🏗️ Faz 1: Temel Mimari, Yetkilendirme & Offline-First Yapı

### 1.1. Proje Altyapısı & Mimari
- [x] GitHub repository kurulumu ve yerel projeye bağlanması
- [x] Splash / Yükleme ekranı (SplashScreen) tasarımı ve animasyonları
- [x] Jetpack Navigation (NavBar / NavHost) rotalarının kurulması
- [ ] MVVM + Clean Architecture temel paket yapısının (di, data, domain, ui) oluşturulması
- [ ] Dependency Injection (Hilt / Koin) entegrasyonu

### 1.2. Kimlik Doğrulama & Kullanıcı Yönetimi (Auth)
- [ ] Firebase projesinin oluşturulması ve `google-services.json` eklenmesi
- [x] **Login Ekranı (Giriş Yap):**
  - [ ] E-posta & Şifre alanları, validator kontrolleri (Email formatı, min 6 karakter)
  - [ ] "Şifremi Unuttum" seçeneği ve sıfırlama e-postası gönderme akışı
  - [ ] Google ile Giriş Yap (Google Sign-In) buton entegrasyonu
- [x] **Register Ekranı (Kayıt Ol):**
  - [ ] Ad-Soyad, E-posta, Şifre ve Şifre Tekrar form alanları
  - [ ] Kullanıcı sözleşmesi ve gizlilik politikası onay kutusu (Checkbox)
- [ ] **Oturum Yönetimi:**
  - [ ] Kullanıcı oturum durumunun (Beni Hatırla / Auto-Login) kontrolü

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
- [ ] **Form Ekranı UI:**
  - [ ] Marka / Model seçimi (Dropdown / AutoComplete)
  - [ ] Yıl, Renk, Şasi No (VIN), Plaka ve Güncel KM input alanları
  - [ ] Araç fotoğrafı ekleme (Galeriden seçme veya Kameradan çekme)
- [ ] Form doğrulama (KM alanı sayısal mı?, Plaka formatı doğru mu?)

### 2.2. Garaj & Çoklu Araç Mantığı
- [ ] Ana ekranda aktif araç kartlarının gösterimi (LazyRow / Carousel)
- [ ] Aktif aracı değiştirme / Varsayılan araç seçme mantığı
- [ ] Araç detay ekranı ve silme/arşivleme opsiyonu

---

## 🛠️ Faz 3: Bakım Takibi & Akıllı Bildirimler

### 3.1. Bakım Kaydı (Maintenance Logging)
- [ ] **Bakım Ekleme Ekranı:**
  - [ ] Yapılan işlem türü (Yağ/Filtre, Fren, Triger, Buji vb.)
  - [ ] İşlem tarihi, yapıldığı KM, kullanılan parça markaları ve toplam ücret
  - [ ] Fatura / Fiş fotoğrafı yükleme alanı
- [ ] Bakım geçmişi listeleme ekranı ve detay sayfası

### 3.2. Akıllı Uyarı & Bildirim Sistemi
- [ ] KM veya zaman bazlı kalan ömür hesaplama algoritması *(Örn: 10.000 km veya 1 yıl)*
- [ ] **Local Push Notifications (Cihaz Bildirimleri):**
  - [ ] Muayene tarihi yaklaşınca bildirim gönderme
  - [ ] Periyodik bakım KM'sine 1.000 km kala uyarı verme

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
- [ ] Sigorta & Kasko bitiş tarihleri, poliçe numarası ve acenta iletişim kartı
- [ ] Poliçe bitimine 30/15/3 gün kala otomatik hatırlatıcılar

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