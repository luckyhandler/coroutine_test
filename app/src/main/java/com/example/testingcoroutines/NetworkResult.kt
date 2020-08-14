package com.example.testingcoroutines

sealed class NetworkResult<T> {
    data class Success<T>(val data: List<Astronaut>) : NetworkResult<T>()
    data class Error<T>(val exception: Exception) : NetworkResult<T>()
}
