package com.paveldolgov.platformscience.shared

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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
 * var mainCoroutineRule = MainCoroutineRule()
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
@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
fun MainCoroutineRule.runBlockingTest(block: suspend () -> Unit) = runTest(this.testDispatcher) {
    block()
}