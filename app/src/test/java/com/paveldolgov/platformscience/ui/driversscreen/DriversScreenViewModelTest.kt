package com.paveldolgov.platformscience.ui.driversscreen

import com.paveldolgov.platformscience.model.Driver
import com.paveldolgov.platformscience.model.DriversAndShipmentsError
import com.paveldolgov.platformscience.repository.DriverRepository
import com.paveldolgov.platformscience.servise.DataRefreshService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DriversScreenViewModelTest {

    @MockK(relaxed = true)
    private lateinit var errorMessageFactory: ErrorMessageFactory

    @MockK(relaxed = true)
    private lateinit var driverRepository: DriverRepository

    @MockK(relaxed = true)
    private lateinit var dataRefreshService: DataRefreshService

    @InjectMockKs
    private lateinit var testObject: DriversScreenViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `createState returns Loading if isLoading and no drivers`() {
        val drivers = emptyList<Driver>()
        val error: DriversAndShipmentsError? = null
        val isLoading = true

        val actual = testObject.createState(drivers, error, isLoading)

        assertEquals(DriversScreenState.Loading, actual)
    }

    @Test
    fun `createState returns Success if isLoading and drivers`() {
        val drivers = listOf<Driver>(mockk())
        val error: DriversAndShipmentsError? = null
        val isLoading = true

        val actual = testObject.createState(drivers, error, isLoading) as DriversScreenState.Success

        assertEquals(drivers, actual.drivers)
        assertEquals(isLoading, actual.isRefreshing)
    }

    @Test
    fun `createState returns UnrecoverableError if is not loading error is NonRetryable and no drivers`() {
        val drivers = emptyList<Driver>()
        val error: DriversAndShipmentsError = DriversAndShipmentsError.NonRetryable
        val isLoading = false

        val actual = testObject.createState(drivers, error, isLoading)

        assertEquals(DriversScreenState.UnrecoverableError, actual)
    }

    @Test
    fun `createState returns Error if is not loading and error is Retryable and no drivers`() {
        val drivers = emptyList<Driver>()
        val error: DriversAndShipmentsError = DriversAndShipmentsError.Retryable
        val isLoading = false
        val errorMessage = "Some Error Message"

        every { errorMessageFactory.getErrorMessage(error) }.returns(errorMessage)

        val actual = testObject.createState(drivers, error, isLoading) as DriversScreenState.Error

        assertEquals(errorMessage, actual.message)
        assertEquals(error, actual.error)
    }
}