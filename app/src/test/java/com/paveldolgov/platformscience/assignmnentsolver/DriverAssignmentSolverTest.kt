package com.paveldolgov.platformscience.assignmnentsolver

import com.paveldolgov.platformscience.model.*
import com.paveldolgov.platformscience.shared.MainCoroutineRule

import com.paveldolgov.platformscience.shared.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DriverAssignmentSolverTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val scoreCalculator = SuitabilityScoreCalculator()
    private val dispatcherProvider =
        provideFakeCoroutinesDispatcherProvider(mainCoroutineRule.testDispatcher)
    private val testObject: DriverAssignmentSolver =
        DriverAssignmentSolver(scoreCalculator, dispatcherProvider)

    // Scores that we can use in our tests to validate algorithm correctness
    //         street "Odd"  "Even" "OddOd" "EvenEvenEv"
    // driver "bbbba"   4.0   1.5     6.0      2.25
    // driver "aaaab"   1.0   6.0     1.5      9.0
    // driver  "aabb"   2.0   4.5     2.0      4.5
    private val driver1 = Driver(DriverId("d1"), "bbbba", "whatever")
    private val driver2 = Driver(DriverId("d2"), "aaaab", "whatever")
    private val driver3 = Driver(DriverId("d3"), "aabb", "whatever")
    private val shipment1 = Shipment(ShipmentId("s1"), Address(1, "Odd"))
    private val shipment2 = Shipment(ShipmentId("s2"), Address(1, "Even"))
    private val shipment3 = Shipment(ShipmentId("s3"), Address(1, "OddOd"))
    private val shipment4 = Shipment(ShipmentId("s4"), Address(1, "EvenEvenEv"))

    @Test
    fun `solve correct score for 2x2`() = runTest {
        val drivers = listOf(driver1, driver2)
        val shipments = listOf(shipment1, shipment2)
        val expected1 = DriverAssignment(driver1, shipment1, 4.0)
        val expected2 = DriverAssignment(driver2, shipment2, 6.0)

        val actual = testObject.solve(drivers, shipments)

        assertTrue(actual.contains(expected1))
        assertTrue(actual.contains(expected2))
    }

    @Test
    fun `solve correct score for 3x2`() = runTest {
        val drivers = listOf(driver1, driver2, driver3)
        val shipments = listOf(shipment1, shipment2)
        val expected1 = DriverAssignment(driver1, shipment1, 4.0)
        val expected2 = DriverAssignment(driver2, shipment2, 6.0)

        val actual = testObject.solve(drivers, shipments)

        assertTrue(actual.contains(expected1))
        assertTrue(actual.contains(expected2))
        assertEquals(2, actual.size)
    }

    @Test
    fun `solve correct score for 3x4`() = runTest {
        val drivers = listOf(driver1, driver2, driver3)
        val shipments = listOf(shipment1, shipment2, shipment3, shipment4)
        val expected1 = DriverAssignment(driver1, shipment3, 6.0)
        val expected2 = DriverAssignment(driver2, shipment4, 9.0)
        val expected3 = DriverAssignment(driver3, shipment2, 4.5)

        val actual = testObject.solve(drivers, shipments)

        assertTrue(actual.contains(expected1))
        assertTrue(actual.contains(expected2))
        assertTrue(actual.contains(expected3))
        assertEquals(3, actual.size)
    }

    @Test
    fun `solve correct score for empty drivers`() = runTest {
        val drivers = emptyList<Driver>()
        val shipments = listOf(shipment1, shipment2, shipment3, shipment4)

        val actual = testObject.solve(drivers, shipments)

        assertEquals(0, actual.size)
    }

    @Test
    fun `solve correct score for empty shipments`() = runTest {
        val drivers = listOf(driver1, driver2, driver3)
        val shipments = emptyList<Shipment>()

        val actual = testObject.solve(drivers, shipments)

        assertEquals(0, actual.size)
    }
}