package com.example.testingcoroutines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class UiStateTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    var observer: Observer<UiState> = mock()

    private lateinit var viewModel: ViewModel
    private lateinit var expectedResult: List<Astronaut>

    @Before
    fun setup() = testCoroutineRule.runBlocking {
        val dataSource = DataSource(dispatchersProvider = testCoroutineRule.testDispatcherProvider)
        val repository = Repository(dataSource = dataSource)
        viewModel = ViewModel(
            dispatcher = testCoroutineRule.testDispatcherProvider.main(),
            repository = repository
        )
        expectedResult = (repository.getData() as NetworkResult.Success).data
        viewModel.stateLiveData.observeForever(observer)
    }

    @Test
    fun `ui states are emitted in the correct order`() = runBlocking {
        viewModel.getData()

        verify(observer).onChanged(UiState.Loading)
        verify(observer).onChanged(UiState.Success(expectedResult))
        verifyNoMoreInteractions(observer)
    }
}