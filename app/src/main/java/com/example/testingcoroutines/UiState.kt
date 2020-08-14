package com.example.testingcoroutines

sealed class UiState {
    object Loading: UiState()
    data class Success(val data: List<Astronaut>): UiState()
    object Error: UiState()
}
