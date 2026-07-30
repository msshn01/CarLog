package com.example.carlog.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carlog.data.repository.CarRepository
import com.example.carlog.model.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CarDataViewModel(
    private val repository: CarRepository = CarRepository()
) : ViewModel() {
    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()
    // Yükleniyor durumu
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Hata mesajı durumu
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // İşlem başarı durumu
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()


    init {
        fetchUserCars()
    }






    /**
     * Kullanıcının araçlarını dinlemeye başlar
     */
    fun fetchUserCars() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getUserCars()
                .catch { exception ->
                    _isLoading.value = false
                    _errorMessage.value = exception.localizedMessage ?: "Araçlar yüklenirken bir hata oluştu."
                }
                .collect { carList ->
                    _cars.value = carList
                    _isLoading.value = false
                }
        }
    }



    fun changeKm(carId: String, newKm: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false
            try {
                repository.changeKm(carId, newKm)
                _isLoading.value = false
                _isSuccess.value = true
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    /**
     * Yeni bir aracı Firestore'a kaydeder.
     */
    fun saveCar(car: Car, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false

            try {
                repository.saveCar(car)
                _isLoading.value = false
                _isSuccess.value = true
                onSuccess()
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.localizedMessage ?: "Araç kaydedilirken bir hata oluştu."
            }
        }
    }

    /**
     * Seçili araca yeni bir bakım kaydı ekler.
     */
    fun addMaintenance(carId: String, maintenanceText: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                repository.addMaintenance(carId, maintenanceText)
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.localizedMessage ?: "Bakım eklenirken bir hata oluştu."
            }
        }
    }

    /**
     * Ekran değiştikten veya hata gösterildikten sonra hata durumunu sıfırlamak için
     */
    fun clearError() {
        _errorMessage.value = null
    }
}