package com.paveldolgov.platformscience.ui.driversscreen

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.paveldolgov.platformscience.model.Driver
import com.paveldolgov.platformscience.model.DriversAndShipmentsError
import com.paveldolgov.platformscience.repository.DriverRepository
import com.paveldolgov.platformscience.servise.DataRefreshService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


@HiltViewModel
class DriversScreenViewModel @Inject constructor(
    private val errorMessageFactory: ErrorMessageFactory,
    private val driverRepository: DriverRepository,
    private val dataRefreshService: DataRefreshService,
) : ViewModel() {

    private val errorState = dataRefreshService.error
    private val loadingState = dataRefreshService.isLoading

    val state: Flow<DriversScreenState> = combine(
        driverRepository.drivers,
        errorState,
        loadingState,
    ) { drivers, error, isLoading ->
        createState(drivers, error, isLoading)
    }

    @VisibleForTesting
    fun createState(
        drivers: List<Driver>,
        error: DriversAndShipmentsError?,
        isLoading: Boolean
    ): DriversScreenState {
        return if (isLoading && drivers.isEmpty()) {
            DriversScreenState.Loading
        } else if (error != null && drivers.isEmpty()) {
            // user can not recover from the error state
            // we handle error only when our drivers list is empty and ignore in all other cases
            when (error) {
                DriversAndShipmentsError.NonRetryable -> DriversScreenState.UnrecoverableError
                DriversAndShipmentsError.Retryable -> DriversScreenState.Error(
                    errorMessageFactory.getErrorMessage(error),
                    error
                )
            }
        } else {
            DriversScreenState.Success(drivers, isLoading)
        }
    }

    /**
     * Requests drivers and shipments. This will request drivers and shipments from the client and trigger assignments rebuild task.
     */
    private fun refreshDriversAndShipments() {
        dataRefreshService.refresh()
    }

    fun onRefreshRequested() {
        refreshDriversAndShipments()
    }

    fun onErrorDialogDismissed(error: DriversAndShipmentsError) {
        when (error) {
            // this should never happen, we can crash early or log an exception, for example
            DriversAndShipmentsError.NonRetryable -> Unit
            DriversAndShipmentsError.Retryable -> refreshDriversAndShipments()
        }
    }
}

sealed class DriversScreenState {
    // Loading state is only shown on initial load when we don't have any data to show
    // we use pull to refresh progress indicator when we have data in the list.
    object Loading : DriversScreenState()
    object UnrecoverableError : DriversScreenState()
    data class Error(val message: String, val error: DriversAndShipmentsError) :
        DriversScreenState()

    data class Success(val drivers: List<Driver>, val isRefreshing: Boolean) :
        DriversScreenState()
}