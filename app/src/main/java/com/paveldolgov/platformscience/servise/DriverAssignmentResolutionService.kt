package com.paveldolgov.platformscience.servise

import com.paveldolgov.platformscience.assignmnentsolver.DriverAssignmentSolver
import com.paveldolgov.platformscience.persistence.adapter.toAppModel
import com.paveldolgov.platformscience.persistence.adapter.toDbModel
import com.paveldolgov.platformscience.persistence.dao.DriverAssignmentDao
import com.paveldolgov.platformscience.persistence.dao.DriverDao
import com.paveldolgov.platformscience.persistence.dao.ShipmentDao
import com.paveldolgov.platformscience.util.CoroutinesDispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverAssignmentResolutionService @Inject constructor(
    private val driverDao: DriverDao,
    private val shipmentDao: ShipmentDao,
    private val driverAssignmentSolver: DriverAssignmentSolver,
    private val driverAssignmentDao: DriverAssignmentDao,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {

    suspend fun calculateAssignment() {
        withContext(dispatcherProvider.io) {
            driverAssignmentDao.deleteAll()
            val drivers = async { driverDao.getDriversList() }
            val shipments = async { shipmentDao.getShipmentsList() }
            val assignments =
                driverAssignmentSolver.solve(drivers.await().map { it.toAppModel() },
                    shipments.await().map { it.toAppModel() })
            driverAssignmentDao.insertAssignments(assignments.map { it.toDbModel() })
        }
    }
}