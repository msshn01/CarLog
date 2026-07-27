package com.example.carlog.userInterface.auth.login


sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val userId: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}