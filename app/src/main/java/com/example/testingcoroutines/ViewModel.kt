package com.example.testingcoroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ViewModel(
    private val dispatcher: CoroutineDispatcher = DefaultDispatchersProvider().main(),
    private val repository: Repository
) : ViewModel() {
    val stateLiveData: LiveData<UiState>
        get() = _stateLiveData
    private val _stateLiveData = MutableLiveData<UiState>()

    fun getData() {
        viewModelScope.launch(dispatcher) {
            _stateLiveData.value = UiState.Loading

            when (val result = repository.getData()) {
                is NetworkResult.Success -> _stateLiveData.value = UiState.Success(data = result.data)
                is NetworkResult.Error -> _stateLiveData.value = UiState.Error
            }
        }
    }
}