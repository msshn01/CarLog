package com.example.carlog.userInterface.auth.register

sealed interface RegisterUiState {
    object Idle : RegisterUiState
    object Loading : RegisterUiState
    data class Success(val userId: String) : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}