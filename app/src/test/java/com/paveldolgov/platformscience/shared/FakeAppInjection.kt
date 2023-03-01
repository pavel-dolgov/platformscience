package com.paveldolgov.platformscience.shared

import com.paveldolgov.platformscience.util.CoroutinesDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher


@ExperimentalCoroutinesApi
fun provideFakeCoroutinesDispatcherProvider(
    dispatcher: TestDispatcher = StandardTestDispatcher()
): CoroutinesDispatcherProvider {
    return CoroutinesDispatcherProvider(
        dispatcher,
        dispatcher,
        dispatcher
    )
}
