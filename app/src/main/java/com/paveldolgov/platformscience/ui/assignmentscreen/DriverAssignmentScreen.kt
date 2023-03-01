package com.paveldolgov.platformscience.ui.assignmentscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paveldolgov.platformscience.R
import com.paveldolgov.platformscience.model.OptionalDriverAssignment
import com.paveldolgov.platformscience.model.ShipmentWithSuitabilityScore
import com.paveldolgov.platformscience.ui.common.Dialog
import com.paveldolgov.platformscience.ui.common.ScreenLoader


@Composable
fun DriverAssignmentScreen(
    viewModel: DriverAssignmentScreenViewModel = hiltViewModel(),
    onNavigateBackRequested: () -> Unit,
) {
    // We don't want to invoke onNavigateBackRequested callback multiple times due to the recomposition as it could lead to
    // unintended behavior, we are using state to guard against that.
    // We can also use something like LaunchedEffect(key1 = Unit, block = { onNavigateBackRequested() })
    // to achieve same behavior, but that feels a little bit overkill.
    val navigateBackInvoked = remember { mutableStateOf(false) }

    when (val state =
        viewModel.state.collectAsStateWithLifecycle(DriverAssignmentScreenState.Loading).value) {
        is DriverAssignmentScreenState.Error -> {
            Dialog(stringResource(id = R.string.not_found), state.message) {
                viewModel.onErrorDialogDismissed()
            }
        }
        DriverAssignmentScreenState.Loading -> ScreenLoader()
        is DriverAssignmentScreenState.Success -> DriverAssignmentItem(state.assignment)
        DriverAssignmentScreenState.Exit -> {
            if (!navigateBackInvoked.value) {
                onNavigateBackRequested()
                navigateBackInvoked.value = true
            }
        }
    }
}

@Composable
fun DriverAssignmentItem(assignment: OptionalDriverAssignment) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = stringResource(id = R.string.driver),
            style = MaterialTheme.typography.bodySmall
        )
        Text(text = assignment.driver.fullName, style = MaterialTheme.typography.titleLarge)
        if (assignment.assignment != null) {
            ShipmentWithScore(assignment.assignment)
        } else {
            NoAssignment()
        }
    }
}

@Composable
fun ShipmentWithScore(shipmentWithScore: ShipmentWithSuitabilityScore) {
    Text(
        text = stringResource(id = R.string.shipment_address),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 16.dp)
    )
    Text(
        text = shipmentWithScore.shipment.address.fullAddress,
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = stringResource(id = R.string.suitability_score),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 16.dp)
    )
    Text(
        text = stringResource(
            R.string.suitability_score_format,
            shipmentWithScore.suitabilityScore
        ),
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun NoAssignment() {
    Text(
        text = stringResource(id = R.string.assignment),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 16.dp)
    )
    Text(
        text = stringResource(id = R.string.no_assignment),
        style = MaterialTheme.typography.titleLarge
    )
}
