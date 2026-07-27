package com.example.carlog.userInterface.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carlog.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // 1. Dışarıya kapalı, değiştirilebilir State
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    // 2. UI ve Navigation'ın okuyabileceği açık State
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun loginUser(email: String, password: String) {
        // Validation (Boş alan kontrolü)
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("E-posta veya şifre boş olamaz.")
            return
        }

        viewModelScope.launch {
            // Yükleniyor durumuna geç
            _uiState.value = LoginUiState.Loading

            // Repository üzerinden Firebase giriş fonksiyonunu çağır
            val result = repository.loginUser(email, password)

            result.onSuccess { userId ->
                _uiState.value = LoginUiState.Success(userId)
            }.onFailure { exception ->
                _uiState.value = LoginUiState.Error(
                    exception.localizedMessage ?: "Giriş yapılırken bir hata oluştu."
                )
            }
        }
    }
}