package com.example.testingcoroutines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class UiStateTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

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
    }

    @Test
    fun `ui states are emitted in the correct order`() = testCoroutineRule.runBlocking {
        viewModel.getData()

        val states = viewModel.stateLiveData.asFlow().take(2).toList()

        assert(states.size == 2)
        assert(states[0] == UiState.Loading)
        assert(states[0] == UiState.Success(expectedResult))
    }
}