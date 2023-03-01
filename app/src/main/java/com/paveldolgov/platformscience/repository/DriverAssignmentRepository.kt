package com.paveldolgov.platformscience.repository

import com.paveldolgov.platformscience.model.DriverId
import com.paveldolgov.platformscience.model.OptionalDriverAssignment
import com.paveldolgov.platformscience.persistence.adapter.toAppModel
import com.paveldolgov.platformscience.persistence.adapter.toDbModel
import com.paveldolgov.platformscience.persistence.dao.DriverAssignmentDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverAssignmentRepository @Inject constructor(
    private val driverAssignmentDao: DriverAssignmentDao
) {

    fun getAssignment(id: DriverId): Flow<OptionalDriverAssignment?> =
        driverAssignmentDao.getDriverAssignmentDetails(id.toDbModel()).map { it?.toAppModel() }
}

