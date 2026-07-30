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

    suspend fun loginUser(email: String, password: String): Boolean {
        // Validation
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("E-posta veya şifre boş olamaz.")
            return false
        }

        // Yükleniyor durumuna geç
        _uiState.value = LoginUiState.Loading

        // 2. 'launch' KULLANMADAN doğrudan repository isteğini atıp bekliyoruz
        val result = repository.loginUser(email, password)

        return result.fold(
            onSuccess = { userId ->
                _uiState.value = LoginUiState.Success(userId)
                true // Başarılıysa true döner
            },
            onFailure = { exception ->
                _uiState.value = LoginUiState.Error(
                    exception.localizedMessage ?: "Giriş yapılırken bir hata oluştu."
                )
                false // Hata varsa false döner
            }
        )
    }

    fun resetPasswordBcsForgot(email : String){
        viewModelScope.launch {
            repository.forgotPassword(email)
            _uiState.value = LoginUiState.Success("Şifre sıfırlama maili gönderildi.")
        }
    }






}