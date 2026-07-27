package com.example.carlog.userInterface.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carlog.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel (private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    // Auth repositoryden yazılan kodlar ile burdan  kayıt yapılır
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun registerUser(email: String, password: String, passConfirm: String){
        if (email.isBlank() ||password.isBlank()){
            _uiState.value = RegisterUiState.Error("Email veya şifre boş olamaz")
            return
        }
        if (password != passConfirm) {
            _uiState.value = RegisterUiState.Error("Şifreler birbiriyle eşleşmiyor.")
            return
        }



        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            val result = repository.registerUser(email, password)
            result.onSuccess { userId ->
                _uiState.value = RegisterUiState.Success(userId)
            }.onFailure { exception ->
                _uiState.value = RegisterUiState.Error(
                    exception.localizedMessage ?: "Kayıt sırasında bir hata oluştu."
                )
            }
        }

    }
}