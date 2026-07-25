# 🚗 AutoLogApp - Proje Yol Haritası & Yapılacaklar Listesi

Uygulama; araç takibi, bakım/gider yönetimi, randevu sistemi, sigorta teklifleri ve AI asistan özelliklerini barındıran kapsamlı bir otomotiv asistanıdır.

---

## 🏗️ Faz 1: Temel Mimarisi & Offline-First Yapı
*İnternet olmasa da veriler kaybolmaz, internet gelince Otomatik Firebase senkronizasyonu yapılır.*

- [ ] **Proje Mimarısı:** MVVM (Model-View-ViewModel) + Clean Architecture yapısının kurulması
- [ ] **Yerel Veritabanı (Room):** İnternet yoksa verilerin cihaza yazılması için SQLite / Room Entity'lerinin oluşturulması
- [ ] **Firebase Entegrasyonu:**
    - [ ] Firebase Authentication (E-posta, Google ile Giriş)
    - [ ] Firestore Database (Bulut senkronizasyonu)
- [ ] **Offline Sync Mantığı (WorkManager):** İnternet bağlantısı geldiğinde cihazdaki lokal verileri arka planda Firestore'a aktaran mekanizmanın yazılması

---

## 🚘 Faz 2: Araç Yönetimi (Vehicle Profile)
- [ ] **Araç Ekleme/Düzenle Ekranı:**
    - [ ] Marka, Model, Yıl, Renk, Şasi No (VIN), Plaka, Güncel KM bilgileri
- [ ] **Çoklu Araç Desteği:** Kullanıcının garajına birden fazla araç ekleyebilmesi

---

## 🛠️ Faz 3: Bakım Takibi & Akıllı Uyarı/Bildirim Sistemi
- [ ] **Bakım Kaydı Oluşturma:** Yapılan bakım, değişen parçalar, tarih, KM ve maliyet girişi
- [ ] **Periyodik Bakım / Hatırlatıcı Sistem:**
    - [ ] KM veya Zaman Bazlı Hatırlatıcılar (Örn: "Yağ değişimine 1.000 km / 15 gün kaldı")
    - [ ] **Local Push Notifications:** Yaklaşan bakımlar, muayene ve sigorta tarihleri için cihaz bildirimi

---

## 💸 Faz 4: Gider Takibi & Finans Yönetimi
- [ ] **Gider Kategorileri:** Akaryakıt, Otopark, Yıkama, Ceza, Aksesuar vb. harcama kayıtları
- [ ] **Grafik & Analiz (Charts):** Aylık/Yıllık araç masraf analiz grafiklerinin gösterimi

---

## 🛡️ Faz 5: Sigorta, Kasko & Reklam Modülü (Monetization)
- [ ] **Sigorta / Kasko Takip Ekranı:** Bitiş tarihleri ve poliçe detayları
- [ ] **Reklam & Acenta Alanı:** Sigorta firmaları / acentalar için özel teklif ve reklam alanları (AdMob veya Sponsorlu Banner)

---

## 📅 Faz 6: Usta Randevu & Servis Sistemi
- [ ] **Usta / Servis Listeleme:** Konuma göre usta ve özel servislerin görüntülenmesi
- [ ] **Randevu Alma Ekranı:** Tarih/saat seçerek servis randevusu oluşturma
- [ ] **Usta Paneli (Opsiyonel):** Ustaların gelen randevuları onaylayıp yönetebileceği temel yapı

---

## 🤖 Faz 7: Yapay Zeka (AI) Chat Asistanı
- [ ] **AI Chat Ekranı (Gemini / OpenAI API):**
    - [ ] Araç arızaları, ikaz lambaları, bakım tavsiyeleri için hazır prompts/sistem talimatları
    - [ ] Kullanıcının aracının marka/model bilgisine göre kişiselleştirilmiş AI yanıtları