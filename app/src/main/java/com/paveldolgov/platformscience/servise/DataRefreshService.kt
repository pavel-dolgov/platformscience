package com.paveldolgov.platformscience.servise

import com.paveldolgov.platformscience.model.DriversAndShipmentsError
import com.paveldolgov.platformscience.util.CoroutinesDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This service responsible for the all local data refresh.
 * 1. Requests new data from network
 * 2. Calculates new assignments based on data received
 * 3. Saves results to the database
 */
@Singleton
class DataRefreshService @Inject constructor(
    private val driversService: DriversAndShipmentsService,
    private val assignmentsService: DriverAssignmentResolutionService,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) {

    private val coroutineScope = CoroutineScope(dispatcherProvider.main)

    private val refreshJobErrorState = MutableStateFlow<DriversAndShipmentsError?>(null)
    val error: Flow<DriversAndShipmentsError?> = refreshJobErrorState

    private val refreshJobState = MutableStateFlow<Job?>(null)
    val isLoading: Flow<Boolean> = refreshJobState.map { it != null }

    fun refresh() {
        if (refreshJobState.value != null) {
            return
        }

        coroutineScope.launch {
            val newJob =
                launch(start = CoroutineStart.LAZY) {
                    driversService.getDriversAndShipments().fold(
                        {
                            refreshJobErrorState.value = it
                        },
                        {
                            assignmentsService.calculateAssignment()
                        }
                    )
                }

            newJob.invokeOnCompletion {
                refreshJobState.compareAndSet(newJob, null)
            }

            if (!newJob.isCompleted && refreshJobState.compareAndSet(null, newJob)) {
                newJob.join()
            }
        }
    }
}