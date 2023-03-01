package com.paveldolgov.platformscience.ui.assignmentscreen

import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.paveldolgov.platformscience.Constants
import com.paveldolgov.platformscience.R
import com.paveldolgov.platformscience.model.DriverId
import com.paveldolgov.platformscience.model.OptionalDriverAssignment
import com.paveldolgov.platformscience.repository.DriverAssignmentRepository
import com.paveldolgov.platformscience.servise.DataRefreshService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class DriverAssignmentScreenViewModel @Inject constructor(
    private val resources: Resources,
    private val driverAssignmentRepository: DriverAssignmentRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dataRefreshService: DataRefreshService,
) : ViewModel() {

    /**
     * Assisted injections are not available in hilt. :(
     * In order to pass id to the view model we can either do it explicitly via a setter,
     * or we can read it from [SavedStateHandle] that hilt knows how to inject into a view model.
     * We can read driver id from [SavedStateHandle] because we save it in navigation route.
     */
    val driverId =
        DriverId(savedStateHandle.get<String>(Constants.DRIVER_ASSIGNMENT_ARGUMENT_KEY)!!)

    private val isExit = MutableStateFlow(false)

    val state: Flow<DriverAssignmentScreenState> = combine(
        driverAssignmentRepository.getAssignment(driverId),
        dataRefreshService.isLoading,
        isExit,
    ) { assignment, isLoading, isExit ->
        createState(assignment, isLoading, isExit)
    }

    @VisibleForTesting
    fun createState(
        assignment: OptionalDriverAssignment?,
        isLoading: Boolean,
        isExit: Boolean
    ): DriverAssignmentScreenState {
        return if (isExit) {
            DriverAssignmentScreenState.Exit
        } else if (isLoading) {
            DriverAssignmentScreenState.Loading
        } else {
            if (assignment == null) {
                DriverAssignmentScreenState.Error(
                    resources.getString(
                        R.string.driver_assignment_not_found_format, driverId.value
                    )
                )
            } else {
                DriverAssignmentScreenState.Success(assignment)
            }
        }
    }

    fun onErrorDialogDismissed() {
        isExit.value = true
    }
}

sealed class DriverAssignmentScreenState {
    object Loading : DriverAssignmentScreenState()
    object Exit : DriverAssignmentScreenState()
    data class Error(val message: String) : DriverAssignmentScreenState()
    data class Success(val assignment: OptionalDriverAssignment) : DriverAssignmentScreenState()
}