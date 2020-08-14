package com.example.testingcoroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Sets the main coroutines dispatcher to a [TestCoroutineDispatcher] for unit testing. A
 * [TestCoroutineDispatcher] provides control over the execution of coroutines.
 *
 * Declare it as a JUnit Rule:
 *
 * ```
 * @get:Rule
 * var mainCoroutineRule = TestCoroutineRule()
 * ```
 *
 * Use the test dispatcher variable to modify the execution of coroutines
 *
 * ```
 * // This pauses the execution of coroutines
 * mainCoroutineRule.testDispatcher.pauseDispatcher()
 * ...
 * // This resumes the execution of coroutines
 * mainCoroutineRule.testDispatcher.resumeDispatcher()
 * ...
 * // This executes the coroutines running on testDispatcher synchronously
 * mainCoroutineRule.runBlocking { }
 * ```
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class TestCoroutineRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {
    val testDispatcherProvider = object : DispatchersProvider {
        override fun default(): CoroutineDispatcher = testDispatcher
        override fun io(): CoroutineDispatcher = testDispatcher
        override fun main(): CoroutineDispatcher = testDispatcher
        override fun unconfined(): CoroutineDispatcher = testDispatcher
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun TestCoroutineRule.runBlocking(block: suspend () -> Unit) = testDispatcher.runBlockingTest {
    block()
}