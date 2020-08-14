package com.example.testingcoroutines

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class DataSource(private val dispatchersProvider: DispatchersProvider) {
    private val moshi = Moshi.Builder().build()

    suspend fun getData(): NetworkResult<List<Astronaut>> {
        return withContext(dispatchersProvider.io()) {
            return@withContext try {
                val classLoader =
                    javaClass.classLoader
                        ?: throw IllegalStateException("classLoader cannot be loaded")
                val json =
                    classLoader.getResource("astros.json").readText()

                val type: Type =
                    Types.newParameterizedType(AstronautWrapper::class.java)

                val result: AstronautWrapper? =
                    moshi.adapter<AstronautWrapper?>(type).fromJson(json)

                if (result == null) {
                    NetworkResult.Error(IllegalStateException("Parsing of json didn't succeed"))
                } else {
                    NetworkResult.Success(data = result.people)
                }
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }
}