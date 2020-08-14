package com.example.testingcoroutines

class Repository(private val dataSource: DataSource) {
    suspend fun getData(): NetworkResult<List<Astronaut>> = dataSource.getData()
}
