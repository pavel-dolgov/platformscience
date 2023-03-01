package com.paveldolgov.platformscience.ui.assignmentscreen

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.paveldolgov.platformscience.Constants.DRIVER_ASSIGNMENT_ARGUMENT_KEY
import com.paveldolgov.platformscience.R
import com.paveldolgov.platformscience.model.OptionalDriverAssignment
import com.paveldolgov.platformscience.repository.DriverAssignmentRepository
import com.paveldolgov.platformscience.servise.DataRefreshService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DriverAssignmentScreenViewModelTest {
    @MockK(relaxed = true)
    private lateinit var resources: Resources

    @MockK(relaxed = true)
    private lateinit var driverAssignmentRepository: DriverAssignmentRepository

    @MockK(relaxed = true)
    private lateinit var dataRefreshService: DataRefreshService

    private val driverIdString = "123"
    private val savedStateHandle =
        SavedStateHandle(mapOf(DRIVER_ASSIGNMENT_ARGUMENT_KEY to driverIdString))

    @InjectMockKs
    private lateinit var testObject: DriverAssignmentScreenViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `createState returns Loading if isLoading`() {
        val assignment: OptionalDriverAssignment = mockk()
        val isLoading = true
        val isExit = false

        val actual = testObject.createState(assignment, isLoading, isExit)

        assertEquals(DriverAssignmentScreenState.Loading, actual)
    }

    @Test
    fun `createState returns Exit if isExit`() {
        val assignment: OptionalDriverAssignment = mockk()
        val isLoading = true
        val isExit = true

        val actual = testObject.createState(assignment, isLoading, isExit)

        assertEquals(DriverAssignmentScreenState.Exit, actual)
    }

    @Test
    fun `createState returns Error if not isLoading not isExit and assignment is null`() {
        val assignment: OptionalDriverAssignment? = null
        val isLoading = false
        val isExit = false
        val message = "Some error message"

        every {
            resources.getString(
                R.string.driver_assignment_not_found_format, driverIdString
            )
        } returns message

        val actual = testObject.createState(
            assignment,
            isLoading,
            isExit
        ) as DriverAssignmentScreenState.Error

        assertEquals(message, actual.message)
    }

    @Test
    fun `createState returns Error if not isLoading not isExit and assignment is not null`() {
        val assignment: OptionalDriverAssignment = mockk()
        val isLoading = false
        val isExit = false

        val actual = testObject.createState(
            assignment,
            isLoading,
            isExit
        ) as DriverAssignmentScreenState.Success

        assertEquals(assignment, actual.assignment)
    }
}