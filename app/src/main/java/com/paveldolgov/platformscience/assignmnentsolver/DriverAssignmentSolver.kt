package com.paveldolgov.platformscience.assignmnentsolver

import com.paveldolgov.platformscience.model.Driver
import com.paveldolgov.platformscience.model.DriverAssignment
import com.paveldolgov.platformscience.model.DriverId
import com.paveldolgov.platformscience.model.Shipment
import com.paveldolgov.platformscience.util.CoroutinesDispatcherProvider
import com.paveldolgov.platformscience.util.getCharsInfo
import kotlinx.coroutines.withContext
import org.jgrapht.alg.matching.KuhnMunkresMinimalWeightBipartitePerfectMatching
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph
import javax.inject.Inject
import kotlin.math.max

class DriverAssignmentSolver @Inject constructor(
    private val scoreCalculator: SuitabilityScoreCalculator,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {

    companion object {
        val DUMMY_DRIVER = Driver(DriverId("#"), "Dummy", "Dummy")
    }

    /**
     * Solves driver to shipment assignment problem to maximize "suitability score".
     * Kuhn-Munkres (Hungarian) algorithm is used to maximize score.
     * Kuhn-Munkres algorithm works with square matrices (in our case it means that
     * number of drivers must match number of shipments), in case if drivers count does not match
     * shipments count, we add dummy drivers/shipments with score 0 to make counts equal.
     */
    suspend fun solve(drivers: List<Driver>, shipments: List<Shipment>): List<DriverAssignment> {
        return withContext(dispatcherProvider.computation) {
            val graph =
                SimpleWeightedGraph<Int, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
            val matrixSize = max(drivers.size, shipments.size)
            val driversVertices = mutableSetOf<Int>()
            val shipmentVertices = mutableSetOf<Int>()

            repeat(matrixSize) { driverIndex ->
                val driver = if (driverIndex in drivers.indices) {
                    drivers[driverIndex]
                } else {
                    DUMMY_DRIVER
                }
                val driverVertex = driverIndex
                driversVertices.add(driverVertex)
                graph.addVertex(driverVertex)
                val driverNameInfo = driver.firstName.getCharsInfo()

                repeat(matrixSize) { shipmentIndex ->
                    val score = if (driver == DUMMY_DRIVER || shipmentIndex !in shipments.indices) {
                        0.0
                    } else {
                        scoreCalculator.calculateScore(
                            shipments[shipmentIndex].address,
                            driverNameInfo
                        )
                    }

                    val shipmentVertex = shipmentIndex + matrixSize
                    shipmentVertices.add(shipmentVertex)
                    graph.addVertex(shipmentVertex)
                    // Kuhn-Munkres algorithm solves minimal cost assignment problem
                    // we reverse weight in order to solve maximum cost problem
                    graph.setEdgeWeight(graph.addEdge(driverVertex, shipmentVertex), -score)
                }
            }

            KuhnMunkresMinimalWeightBipartitePerfectMatching(
                graph,
                driversVertices,
                shipmentVertices
            ).matching.edges
                // we are maximizing scores, 0 weight edges coming from dummy drivers/shipments
                .filterNot { edge -> graph.getEdgeWeight(edge) == 0.0 }
                .map { edge ->
                    val driverIndex = graph.getEdgeSource(edge)
                    val shipmentIndex = graph.getEdgeTarget(edge) - matrixSize
                    val driver = drivers[driverIndex]
                    DriverAssignment(
                        driver,
                        shipments[shipmentIndex],
                        -graph.getEdgeWeight(edge)
                    )
                }
        }
    }
}