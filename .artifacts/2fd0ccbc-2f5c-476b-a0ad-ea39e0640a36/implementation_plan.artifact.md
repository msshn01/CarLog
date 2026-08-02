# Bakım Ekleme Ekranı Uygulama Planı

Bu plan, uygulamaya yeni bir bakım kaydı ekleme özelliği kazandırmayı amaçlamaktadır. Kullanıcı ana ekranda bir araç seçtiğinde "Bakım Ekle" butonuna basarak bu ekrana gidebilecek ve girdiği veriler ilgili aracın bakım listesine kaydedilecektir.

## Proposed Changes

### [Component: Screens]

#### [NEW] [AddMaintenanceScreen.kt](file:///Users/musa/AndroidStudioProjects/CarLog/app/src/main/java/com/example/carlog/screen/AddMaintenanceScreen.kt)
*   Seçili aracın `carId` bilgisini alacak.
*   `title` (İşlem Adı), `description` (Detay), `price` (Tutar) ve `date` (Tarih) alanları için `OutlinedTextField` içerecek.
*   Tasarım, projenin genel temasına (`AddCarScreen` ile tutarlı) uygun olacak (Yeşil renk vurgulu).
*   Kaydet butonuna tıklandığında `CarDataViewModel.addMaintenance` fonksiyonunu çağıracak.

#### [MODIFY] [MainScreen.kt](file:///Users/musa/AndroidStudioProjects/CarLog/app/src/main/java/com/example/carlog/screen/MainScreen.kt)
*   `AddLogFloatingActionButton` içindeki `navController.navigate("addCar")` yönlendirmesi, seçili araç varsa `addMaintenance/{carId}` olarak güncellenecek.
*   `InfoStatLazy` composable'ı, hardcoded veriler yerine `Maintenance` listesindeki gerçek verileri (`item.title`, `item.description` vb.) kullanacak şekilde düzeltilecek.

### [Component: Navigation]

#### [MODIFY] [NavComposable.kt](file:///Users/musa/AndroidStudioProjects/CarLog/app/src/main/java/com/example/carlog/nav/NavComposable.kt)
*   `addMaintenance/{carId}` rotası eklenecek.
*   Bu rota `AddMaintenanceScreen`'i çağıracak ve `carId` argümanını iletecek.

## Verification Plan

### Manual Verification
1.  Uygulamayı çalıştırın.
2.  Ana ekranda bir araç seçin.
3.  Sağ alttaki "Bakım Ekle" butonuna basın.
4.  Açılan ekranda bakım bilgilerini girin ve "Bakımı Kaydet" butonuna basın.
5.  Ana ekrana döndüğünüzde, alt kısımdaki "Araç bakım kayıtları" listesinde yeni eklediğiniz kaydın göründüğünü doğrulayın.
